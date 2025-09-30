package com.game.jumper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import com.game.jumper.JumperGame
import com.game.jumper.utils.Constants

/**
 * Instructions screen - shows how to play the game
 */
class InstructionsScreen(private val game: JumperGame) : Screen {

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
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

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

        // Draw UI text
        game.batch.projectionMatrix = camera.combined
        game.batch.begin()

        // Helper function for shadowed text
        fun drawText(text: String, y: Float, scale: Float = 2f, align: Int = Align.left) {
            game.font.data.setScale(scale)
            game.font.color = Constants.UI_SHADOW_COLOR
            game.font.draw(game.batch, text, 12f, y - 2f, JumperGame.GAME_WIDTH - 20, align, true)
            game.font.color = Constants.UI_TEXT_COLOR
            game.font.draw(game.batch, text, 10f, y, JumperGame.GAME_WIDTH - 20, align, true)
        }

        // Title
        game.font.data.setScale(3f)
        game.font.color = Constants.UI_SHADOW_COLOR
        game.font.draw(game.batch, "HOW TO PLAY", 2f, JumperGame.GAME_HEIGHT - 48f, JumperGame.GAME_WIDTH, Align.center, false)
        game.font.color = Constants.UI_TEXT_COLOR
        game.font.draw(game.batch, "HOW TO PLAY", 0f, JumperGame.GAME_HEIGHT - 50f, JumperGame.GAME_WIDTH, Align.center, false)

        // Instructions
        var yPos = JumperGame.GAME_HEIGHT - 120f

        drawText("OBJECTIVE:", yPos, 2.2f)
        yPos -= 35f
        drawText("Stay alive! You have 3 lives.", yPos, 1.7f)
        yPos -= 30f
        drawText("Avoid ground & enemies!", yPos, 1.7f)
        yPos -= 55f

        drawText("CONTROLS:", yPos, 2.2f)
        yPos -= 35f
        drawText("• LEFT: Jump (3x in air)", yPos, 1.7f)
        yPos -= 30f
        drawText("• RIGHT: Shoot enemies", yPos, 1.7f)
        yPos -= 30f
        drawText("• CLICK ITEMS: Collect", yPos, 1.7f)
        yPos -= 55f

        drawText("ENEMIES:", yPos, 2.2f)
        yPos -= 35f
        drawText("• Purple diamonds shoot!", yPos, 1.6f)
        yPos -= 28f
        drawText("• Takes 2 hits to destroy", yPos, 1.6f)
        yPos -= 50f

        drawText("ITEMS:", yPos, 2.2f)
        yPos -= 35f
        drawText("• GOLD: Weapon upgrades", yPos, 1.6f)
        yPos -= 28f
        drawText("• SKY BLUE: Power jump", yPos, 1.6f)
        yPos -= 28f
        drawText("• PINK HEART: Restore life", yPos, 1.6f)

        // Back button
        game.font.data.setScale(2f)
        game.font.color = Constants.UI_SHADOW_COLOR
        game.font.draw(game.batch, "TAP TO GO BACK", 2f, 48f, JumperGame.GAME_WIDTH, Align.center, false)
        game.font.color = Constants.UI_TEXT_COLOR
        game.font.draw(game.batch, "TAP TO GO BACK", 0f, 50f, JumperGame.GAME_WIDTH, Align.center, false)

        game.batch.end()

        // Handle input
        if (Gdx.input.justTouched()) {
            game.screen = MenuScreen(game)
            dispose()
        }
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}