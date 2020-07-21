package com.microsoft.device.dualscreen.configchangetest

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.Region
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.device.display.DisplayMask
import java.lang.IllegalStateException


class MainActivity : AppCompatActivity() {

    var TAG = "DUALSCREEN_CONFIGCHANGE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        var ias = isAppSpannedEx(this)
        Log.d(TAG, "onConfigurationChanged isAppSpanned " + ias + " " + newConfig)

        findViewById<TextView>(R.id.helloWorld).text = ias.toString()
    }

    override fun onResume() {
        super.onResume()
        var ias = isAppSpannedEx(this)
        Log.d(TAG, "onResume isAppSpanned " + ias)

        findViewById<TextView>(R.id.helloWorld).text = ias.toString()
    }

    var FXTAG = "DUALSCREEN_CONFIGCHANGE"
    // https://docs.microsoft.com/dual-screen/android/sample-code/is-dual-screen-device
    fun isDeviceSurfaceDuo(): Boolean {
        val feature = "com.microsoft.device.display.displaymask"
        val pm = this.packageManager
        return if (pm.hasSystemFeature(feature)) {
            Log.i(FXTAG, "System has feature: $feature")
            true
        } else {
            Log.w(FXTAG, "System missing feature: $feature")
            false
        }
    }
    fun getCurrentRotation(activity: Activity): Int {
        return try {
            val wm = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.rotation
        } catch (e: IllegalStateException) { Surface.ROTATION_0 }
    }
    fun getHinge(activity: Activity): Rect? {
        // Hinge's coordinates of its 4 edges in different mode
        // Double Landscape Rect(0, 1350 - 1800, 1434)
        // Double Portrait  Rect(1350, 0 - 1434, 1800)
        return if (isDeviceSurfaceDuo()) {
            val displayMask = DisplayMask.fromResourcesRectApproximation(activity)
            if (displayMask != null) {
                val screensBounding = displayMask.getBoundingRectsForRotation(
                        getCurrentRotation(activity)
                )
                if (screensBounding.size == 0) {
                    Rect(0, 0, 0, 0)
                } else screensBounding[0]
            } else { null }
        } else { null }
    }
    fun getWindowRect(activity: Activity): Rect {
        val windowRect = Rect()
        activity.windowManager.defaultDisplay.getRectSize(windowRect)
        return windowRect
    }
    fun isAppSpannedEx(activity: Activity): Boolean {
        val hinge = getHinge(activity)
        val windowRect = getWindowRect(activity)

        return if (hinge != null && windowRect.width() > 0 && windowRect.height() > 0) {
            // The windowRect doesn't intersect hinge
            hinge.intersect(windowRect)
        } else false
    }

    // DOES NOT WORK
    private fun isAppSpanned(): Boolean {
        val displayMask: DisplayMask = DisplayMask.fromResourcesRectApproximation(this)
        val region: Region = displayMask.getBounds()
        val boundings: List<Rect> = displayMask.getBoundingRects()
        if (boundings.isEmpty()) {
            Log.d(TAG, "Dual screen - no boundings")
            return false
        }
        val first: Rect = boundings[0]
        val rootView: View = this.window.decorView.rootView
        val drawingRect = Rect()
        rootView.getDrawingRect(drawingRect)
        return if (first.intersect(drawingRect)) {
            Log.d(TAG, "Dual screen - intersect: $drawingRect")
            true
        } else {
            Log.d(TAG, "Single screen - not intersect: $drawingRect")
            false
        }
    }
}