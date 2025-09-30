package com.game.jumper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.Align
import com.game.jumper.JumperGame
import com.game.jumper.managers.GameStateManager
import com.game.jumper.utils.Constants

/**
 * Game over screen - shown after player dies
 * Displays final score and options to continue or watch rewarded ad
 */
class GameOverScreen(private val game: JumperGame) : Screen {

    private val camera = OrthographicCamera().apply {
        setToOrtho(false, JumperGame.GAME_WIDTH, JumperGame.GAME_HEIGHT)
    }

    private var gameOverCount = 0

    override fun show() {
        gameOverCount++

        // Show interstitial ad every N game overs
        if (gameOverCount % Constants.SHOW_INTERSTITIAL_EVERY == 0) {
            game.getAdManager().showInterstitialAd()
        }

        // Load new ads for next time
        game.getAdManager().loadInterstitialAd()
        game.getAdManager().loadRewardedAd()
    }

    override fun render(delta: Float) {
        // Clear screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.projectionMatrix = camera.combined

        // Draw UI
        game.batch.begin()

        // Game Over title
        val gameOverText = "GAME OVER"
        game.font.draw(
            game.batch,
            gameOverText,
            0f,
            JumperGame.GAME_HEIGHT - 100f,
            JumperGame.GAME_WIDTH,
            Align.center,
            false
        )

        // Final score
        val scoreText = "SCORE: ${GameStateManager.getCurrentScore()}"
        game.font.draw(
            game.batch,
            scoreText,
            0f,
            JumperGame.GAME_HEIGHT / 2f + 100f,
            JumperGame.GAME_WIDTH,
            Align.center,
            false
        )

        // High score
        val highScoreText = "HIGH SCORE: ${GameStateManager.getHighScore()}"
        game.font.draw(
            game.batch,
            highScoreText,
            0f,
            JumperGame.GAME_HEIGHT / 2f + 50f,
            JumperGame.GAME_WIDTH,
            Align.center,
            false
        )

        // Options
        val playAgainText = "TAP TO PLAY AGAIN"
        game.font.draw(
            game.batch,
            playAgainText,
            0f,
            JumperGame.GAME_HEIGHT / 2f - 50f,
            JumperGame.GAME_WIDTH,
            Align.center,
            false
        )

        // Rewarded ad option
        if (game.getAdManager().isRewardedAdReady()) {
            val rewardText = "OR"
            game.font.draw(
                game.batch,
                rewardText,
                0f,
                JumperGame.GAME_HEIGHT / 2f - 100f,
                JumperGame.GAME_WIDTH,
                Align.center,
                false
            )

            val watchAdText = "WATCH AD FOR BONUS"
            game.font.draw(
                game.batch,
                watchAdText,
                0f,
                JumperGame.GAME_HEIGHT / 2f - 150f,
                JumperGame.GAME_WIDTH,
                Align.center,
                false
            )
        }

        // Menu option
        val menuText = "OR RETURN TO MENU"
        game.font.draw(
            game.batch,
            menuText,
            0f,
            100f,
            JumperGame.GAME_WIDTH,
            Align.center,
            false
        )

        game.batch.end()

        // Handle input
        if (Gdx.input.justTouched()) {
            val touchY = Gdx.input.y
            val screenHeight = Gdx.graphics.height

            // Convert touch coordinates (top-left origin) to game coordinates (bottom-left origin)
            val gameY = JumperGame.GAME_HEIGHT * (1f - touchY.toFloat() / screenHeight)

            when {
                // Play again
                gameY > JumperGame.GAME_HEIGHT / 2f - 100f -> {
                    game.screen = GameScreen(game)
                    dispose()
                }
                // Watch ad
                gameY > 150f && game.getAdManager().isRewardedAdReady() -> {
                    game.getAdManager().showRewardedAd {
                        // Give bonus score
                        GameStateManager.setCurrentScore(
                            GameStateManager.getCurrentScore() + 10
                        )
                    }
                }
                // Return to menu
                else -> {
                    game.screen = MenuScreen(game)
                    dispose()
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}