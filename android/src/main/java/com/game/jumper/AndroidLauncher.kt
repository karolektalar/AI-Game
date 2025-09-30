package com.game.jumper

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.game.jumper.managers.AdManager

/**
 * Android launcher - entry point for the Android application
 */
class AndroidLauncher : AndroidApplication() {

    private lateinit var adManager: AndroidAdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize AdMob
        adManager = AndroidAdManager(this)

        // Configure LibGDX
        val config = AndroidApplicationConfiguration().apply {
            useAccelerometer = false
            useCompass = false
            useGyroscope = false
        }

        // Initialize and start the game
        initialize(JumperGame(adManager), config)
    }

    override fun onResume() {
        super.onResume()
        adManager.resume()
    }

    override fun onPause() {
        super.onPause()
        adManager.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adManager.destroy()
    }
}