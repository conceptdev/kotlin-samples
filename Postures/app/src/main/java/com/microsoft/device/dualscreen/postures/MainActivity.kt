package com.microsoft.device.dualscreen.postures

import android.content.res.Configuration
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_HINGE_ANGLE
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.window.WindowManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.microsoft.device.display.DisplayMask
import kotlinx.android.synthetic.main.fragment_first.*
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {

    private lateinit var wm: WindowManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        setupSensors()

        // Jetpack Window Manager
        wm = WindowManager(this, null)
        wm.registerDeviceStateChangeCallback(
            runOnUiThreadExecutor(),
            { deviceState ->
                jwm_posture.text = "Posture: ${deviceState.toString()}"
            })

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // DisplayMask
        val displayMask: DisplayMask = DisplayMask.fromResourcesRect(this)
        val masks: List<Rect> = displayMask.getBoundingRectsForRotation(0)
        var mask = Rect()
        if (masks.isEmpty()) {
            dm_mask.text = "not spanned"
        } else {
            mask = masks[0]
            dm_mask.text = "spanned, hinge: ${mask.toString()}"
        }
    }

    // Jetpack Window Manager
    private fun runOnUiThreadExecutor(): Executor {
        val handler = Handler(Looper.getMainLooper())
        return Executor() {
            handler.post(it)
        }
    }
    // Jetpack Window Manager
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        wm.registerLayoutChangeCallback(
            runOnUiThreadExecutor(),
            { windowLayoutInfo ->
                jwm_hinge.text = windowLayoutInfo.toString()
            })
    }


    private val HINGE_ANGLE_SENSOR_NAME = "Hinge Angle"

    private var mSensorManager: SensorManager? = null
    private var mHingeAngleSensor: Sensor? = null
    private var mSensorListener: SensorEventListener? = null


    private fun setupSensors() {
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensorList: List<Sensor> = mSensorManager!!.getSensorList(Sensor.TYPE_ALL)
        for (sensor in sensorList) {
            if (sensor.getName().contains(HINGE_ANGLE_SENSOR_NAME)) {
                mHingeAngleSensor = sensor
            }
        }
        mSensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor == mHingeAngleSensor) {
                    val angle = event.values[0].toInt()
                    dm_angle.text = "Hinge angle: ${angle}"
                    if (angle < 10)
                        dm_posture.text = "Closed"
                    else if (angle < 75)
                        dm_posture.text = "Peek"
                    else if (angle < 160)
                        dm_posture.text = "Concave / half-open"
                    else if (angle < 235)
                        dm_posture.text = "Flat / open"
                    else
                        dm_posture.text = "Convex / flipped"
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                //TODO
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (mHingeAngleSensor != null) {
            mSensorManager?.unregisterListener(mSensorListener, mHingeAngleSensor)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mHingeAngleSensor != null) {
            mSensorManager?.registerListener(
                mSensorListener,
                mHingeAngleSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}