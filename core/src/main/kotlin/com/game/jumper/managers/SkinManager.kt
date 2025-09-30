package com.game.jumper.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.game.jumper.entities.PlayerSkin

/**
 * Manages player skin selection and persistence
 */
object SkinManager {
    private const val PREFS_NAME = "JumperGamePrefs"
    private const val SKIN_KEY = "selectedSkin"

    private lateinit var prefs: Preferences
    private var currentSkin = PlayerSkin.DEFAULT

    fun init() {
        prefs = Gdx.app.getPreferences(PREFS_NAME)
        val savedSkin = prefs.getString(SKIN_KEY, PlayerSkin.DEFAULT.name)
        currentSkin = try {
            PlayerSkin.valueOf(savedSkin)
        } catch (e: Exception) {
            PlayerSkin.DEFAULT
        }
    }

    fun getCurrentSkin(): PlayerSkin = currentSkin

    fun setCurrentSkin(skin: PlayerSkin) {
        currentSkin = skin
        prefs.putString(SKIN_KEY, skin.name)
        prefs.flush()
    }
}