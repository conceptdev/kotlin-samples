/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.experiments

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.util.Consumer
import androidx.window.DisplayFeature
import androidx.window.FoldingFeature
import androidx.window.WindowLayoutInfo
import androidx.window.WindowManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.ui.StyledPlayerView
import java.util.concurrent.Executor


class MainActivity : Activity() {
    private lateinit var motionLayout: MotionLayout
    private lateinit var windowManager: WindowManager
    private val handler = Handler(Looper.getMainLooper())
    private val mainThreadExecutor = Executor { r: Runnable -> handler.post(r) }
    private val stateContainer = StateContainer()

    private lateinit var playerView : StyledPlayerView
    private lateinit var controlView : StyledPlayerControlView
    private lateinit var player : SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        windowManager = WindowManager(this)

        setContentView(R.layout.activity_main2)
        motionLayout = findViewById<MotionLayout>(R.id.root)


        playerView = findViewById(R.id.player_view);
        // Instantiate the player.
        player = SimpleExoPlayer.Builder(this).build()
        // Attach player to the view.
        playerView.player = player

        // Separate controller on second screen
        controlView = findViewById(R.id.player_control_view)
        controlView.player = player
    }

    override fun onStart() {
        super.onStart()
        windowManager.registerLayoutChangeCallback(mainThreadExecutor, stateContainer)

        // Set the media source to be played.
        var videoUrl = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
        var mediaItem = MediaItem.fromUri(videoUrl)
        player.setMediaItem(mediaItem)
        player.prepare()
        //player.play()
    }

    override fun onStop() {
        super.onStop()
        windowManager.unregisterLayoutChangeCallback(stateContainer)
    }

    /**
     * Returns the position of the fold relative to the view
     */
    fun foldPosition(view: View, foldingFeature: FoldingFeature): Int {
        val splitRect = getFeatureBoundsInWindow(foldingFeature, view)
        splitRect?.let {
            return view.height.minus(splitRect.top)
        }

        return 0
    }

    /**
     * Get the bounds of the display feature translated to the View's coordinate space and current
     * position in the window. This will also include view padding in the calculations.
     */
    fun getFeatureBoundsInWindow(
        displayFeature: DisplayFeature,
        view: View,
        includePadding: Boolean = true
    ): Rect? {
        // The the location of the view in window to be in the same coordinate space as the feature.
        val viewLocationInWindow = IntArray(2)
        view.getLocationInWindow(viewLocationInWindow)

        // Intersect the feature rectangle in window with view rectangle to clip the bounds.
        val viewRect = Rect(
            viewLocationInWindow[0], viewLocationInWindow[1],
            viewLocationInWindow[0] + view.width, viewLocationInWindow[1] + view.height
        )

        // Include padding if needed
        if (includePadding) {
            viewRect.left += view.paddingLeft
            viewRect.top += view.paddingTop
            viewRect.right -= view.paddingRight
            viewRect.bottom -= view.paddingBottom
        }

        val featureRectInView = Rect(displayFeature.bounds)
        val intersects = featureRectInView.intersect(viewRect)

        //Checks to see if the display feature overlaps with our view at all
        if ((featureRectInView.width() == 0 && featureRectInView.height() == 0) ||
            !intersects
        ) {
            return null
        }

        // Offset the feature coordinates to view coordinate space start point
        featureRectInView.offset(-viewLocationInWindow[0], -viewLocationInWindow[1])

        return featureRectInView
    }

    inner class StateContainer : Consumer<WindowLayoutInfo> {
        var lastLayoutInfo: WindowLayoutInfo? = null

        override fun accept(newLayoutInfo: WindowLayoutInfo) {

            // Add views that represent display features
            for (displayFeature in newLayoutInfo.displayFeatures) {
                val foldFeature = displayFeature as? FoldingFeature
                if (foldFeature != null) {
                    if (foldFeature.isSeparating && // isSeparating not responding as expected on 7" & 8" emulators...
                        foldFeature.orientation == FoldingFeature.ORIENTATION_HORIZONTAL
                    ) {
                        if (isDeviceSurfaceDuo())
                        {
                            //ORIG: this code _should_ work all on devices....
                            // The foldable device is in tabletop mode
                            val fold = foldPosition(motionLayout, foldFeature)
                            ConstraintLayout.getSharedValues().fireNewValue(R.id.fold, fold)
                            playerView.useController = false // use other screen controls
                        }
                        else {
                            //HACK: required by the foldable emulators, which don't seem to honor isSeparating right now...???
                            if (foldFeature.state == FoldingFeature.STATE_HALF_OPENED) { // this replaces isSeparating (temporarily)
                                val fold = foldPosition(motionLayout, foldFeature)
                                ConstraintLayout.getSharedValues().fireNewValue(R.id.fold, fold)
                                playerView.useController = false // use other screen controls
                            } else {
                                ConstraintLayout.getSharedValues().fireNewValue(R.id.fold, 0);
                                playerView.useController = true // use on-video controls
                            }
                        }
                    } else {
                        ConstraintLayout.getSharedValues().fireNewValue(R.id.fold, 0);
                        playerView.useController = true // use on-video controls
                    }
                }
            }
        }
        /**
         * HACK just to help with testing on Surface Duo AND foldable emulators until WM is stable */
        fun isDeviceSurfaceDuo(): Boolean {
            val surfaceDuoSpecificFeature = "com.microsoft.device.display.displaymask"
            val pm = this@MainActivity.packageManager
            return pm.hasSystemFeature(surfaceDuoSpecificFeature)
        }
    }
}