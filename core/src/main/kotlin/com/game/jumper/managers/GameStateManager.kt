package com.game.jumper.managers

/**
 * Manages game state including score and high score persistence
 */
object GameStateManager {
    private var currentScore = 0
    private var highScore = 0

    fun getCurrentScore(): Int = currentScore

    fun getHighScore(): Int = highScore

    fun setCurrentScore(score: Int) {
        currentScore = score
        if (score > highScore) {
            highScore = score
        }
    }

    fun incrementScore() {
        currentScore++
        if (currentScore > highScore) {
            highScore = currentScore
        }
    }

    fun resetCurrentScore() {
        currentScore = 0
    }

    // Called from Android preferences system
    fun loadHighScore(score: Int) {
        if (score > highScore) {
            highScore = score
        }
    }
}