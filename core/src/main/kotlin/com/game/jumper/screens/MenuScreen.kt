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
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()

        // Draw gradient background
        game.shapeRenderer.projectionMatrix = camera.combined
        game.shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled)

        val gradientSteps = 20
        val stepHeight = JumperGame.GAME_HEIGHT / gradientSteps
        for (i in 0 until gradientSteps) {
            val ratio = i.toFloat() / gradientSteps
            val r = Constants.BACKGROUND_TOP.r + (Constants.BACKGROUND_BOTTOM.r - Constants.BACKGROUND_TOP.r) * ratio
            val g = Constants.BACKGROUND_TOP.g + (Constants.BACKGROUND_BOTTOM.g - Constants.BACKGROUND_TOP.g) * ratio
            val b = Constants.BACKGROUND_TOP.b + (Constants.BACKGROUND_BOTTOM.b - Constants.BACKGROUND_TOP.b) * ratio
            game.shapeRenderer.setColor(r, g, b, 1f)
            game.shapeRenderer.rect(0f, JumperGame.GAME_HEIGHT - (i + 1) * stepHeight, JumperGame.GAME_WIDTH, stepHeight)
        }

        game.shapeRenderer.end()

        // Draw UI
        game.batch.projectionMatrix = camera.combined
        game.batch.begin()

        // Helper for shadowed text
        fun drawText(text: String, y: Float, scale: Float = 2f) {
            game.font.data.setScale(scale)
            game.font.color = Constants.UI_SHADOW_COLOR
            game.font.draw(game.batch, text, 2f, y - 2f, JumperGame.GAME_WIDTH, Align.center, false)
            game.font.color = Constants.UI_TEXT_COLOR
            game.font.draw(game.batch, text, 0f, y, JumperGame.GAME_WIDTH, Align.center, false)
        }

        // Title
        drawText(JumperGame.GAME_NAME, JumperGame.GAME_HEIGHT - 100f, 4f)

        // Play button
        drawText("TAP TO PLAY", JumperGame.GAME_HEIGHT / 2f + 80f, 2.5f)

        // High score
        val highScore = "HIGH SCORE: ${com.game.jumper.managers.GameStateManager.getHighScore()}"
        drawText(highScore, JumperGame.GAME_HEIGHT / 2f + 20f, 2f)

        // Skins button
        drawText("SKINS", JumperGame.GAME_HEIGHT / 2f - 60f, 2f)

        // Instructions button
        drawText("HOW TO PLAY", JumperGame.GAME_HEIGHT / 2f - 120f, 2f)

        game.batch.end()

        // Check for input
        if (Gdx.input.justTouched()) {
            val touchY = Gdx.input.y
            val screenHeight = Gdx.graphics.height
            val gameY = JumperGame.GAME_HEIGHT * (1f - touchY.toFloat() / screenHeight)

            when {
                // Play
                gameY > JumperGame.GAME_HEIGHT / 2f + 50f -> {
                    game.screen = GameScreen(game)
                    dispose()
                }
                // Skins
                gameY > JumperGame.GAME_HEIGHT / 2f - 80f && gameY <= JumperGame.GAME_HEIGHT / 2f - 40f -> {
                    game.screen = SkinSelectionScreen(game)
                    dispose()
                }
                // Instructions
                gameY <= JumperGame.GAME_HEIGHT / 2f - 100f -> {
                    game.screen = InstructionsScreen(game)
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