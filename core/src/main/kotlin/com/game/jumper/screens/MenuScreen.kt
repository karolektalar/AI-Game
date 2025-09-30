package com.game.jumper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.Align
import com.game.jumper.JumperGame
import com.game.jumper.utils.Constants

/**
 * Main menu screen - shown when game starts
 * Tap anywhere to start playing
 */
class MenuScreen(private val game: JumperGame) : Screen {

    private val camera = OrthographicCamera().apply {
        setToOrtho(false, JumperGame.GAME_WIDTH, JumperGame.GAME_HEIGHT)
    }

    override fun show() {}

    override fun render(delta: Float) {
        // Clear screen
        Gdx.gl.glClearColor(
            Constants.BACKGROUND_COLOR.r,
            Constants.BACKGROUND_COLOR.g,
            Constants.BACKGROUND_COLOR.b,
            1f
        )
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.projectionMatrix = camera.combined

        // Draw UI
        game.batch.begin()

        // Title
        val title = JumperGame.GAME_NAME
        game.font.draw(
            game.batch,
            title,
            0f,
            JumperGame.GAME_HEIGHT - 100f,
            JumperGame.GAME_WIDTH,
            Align.center,
            false
        )

        // Instructions
        val instructions = "TAP TO START"
        game.font.draw(
            game.batch,
            instructions,
            0f,
            JumperGame.GAME_HEIGHT / 2f,
            JumperGame.GAME_WIDTH,
            Align.center,
            false
        )

        // High score
        val highScore = "HIGH SCORE: ${game.getAdManager().let { com.game.jumper.managers.GameStateManager.getHighScore() }}"
        game.font.draw(
            game.batch,
            highScore,
            0f,
            JumperGame.GAME_HEIGHT / 2f - 100f,
            JumperGame.GAME_WIDTH,
            Align.center,
            false
        )

        game.batch.end()

        // Check for input
        if (Gdx.input.justTouched()) {
            game.screen = GameScreen(game)
            dispose()
        }
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}