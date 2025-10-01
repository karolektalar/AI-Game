package com.game.jumper.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

/**
 * Manages game state including score and high score persistence
 * Thread-safe singleton for score management
 */
object GameStateManager {
    private const val PREFS_NAME = "JumperGamePrefs"
    private const val HIGH_SCORE_KEY = "highScore"

    private var currentScore = 0
    private var highScore = 0
    private lateinit var prefs: Preferences

    /**
     * Initialize the manager and load saved high score
     * Must be called before using any other methods
     */
    fun init() {
        prefs = Gdx.app.getPreferences(PREFS_NAME)
        highScore = prefs.getInteger(HIGH_SCORE_KEY, 0)
    }

    @Synchronized
    fun getCurrentScore(): Int = currentScore

    @Synchronized
    fun getHighScore(): Int = highScore

    @Synchronized
    fun setCurrentScore(score: Int) {
        currentScore = score
        if (score > highScore) {
            highScore = score
            saveHighScore()
        }
    }

    @Synchronized
    fun incrementScore() {
        currentScore++
        if (currentScore > highScore) {
            highScore = currentScore
            saveHighScore()
        }
    }

    @Synchronized
    fun resetCurrentScore() {
        currentScore = 0
    }

    /**
     * Persist high score to disk
     */
    private fun saveHighScore() {
        prefs.putInteger(HIGH_SCORE_KEY, highScore)
        prefs.flush()
    }
}