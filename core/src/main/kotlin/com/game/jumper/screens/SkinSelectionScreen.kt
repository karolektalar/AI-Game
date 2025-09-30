package com.game.jumper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import com.game.jumper.JumperGame
import com.game.jumper.entities.PlayerSkin
import com.game.jumper.managers.SkinManager
import com.game.jumper.utils.Constants

/**
 * Skin selection screen - allows player to choose their skin
 */
class SkinSelectionScreen(private val game: JumperGame) : Screen {

    private val camera = OrthographicCamera().apply {
        setToOrtho(false, JumperGame.GAME_WIDTH, JumperGame.GAME_HEIGHT)
    }

    private val skins = PlayerSkin.values()
    private val skinBoxSize = 80f
    private val skinsPerRow = 3
    private val startX = (JumperGame.GAME_WIDTH - (skinsPerRow * (skinBoxSize + 20f))) / 2
    private val startY = JumperGame.GAME_HEIGHT - 200f

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

        // Draw skin boxes
        skins.forEachIndexed { index, skin ->
            val col = index % skinsPerRow
            val row = index / skinsPerRow
            val x = startX + col * (skinBoxSize + 20f)
            val y = startY - row * (skinBoxSize + 40f)

            val isSelected = SkinManager.getCurrentSkin() == skin

            // Box outline
            if (isSelected) {
                game.shapeRenderer.setColor(1f, 1f, 1f, 1f)
                game.shapeRenderer.rect(x - 4, y - 4, skinBoxSize + 8, skinBoxSize + 8)
            }

            // Main box with skin colors
            game.shapeRenderer.setColor(skin.primaryColor)
            game.shapeRenderer.rect(x, y, skinBoxSize, skinBoxSize)

            // Outline
            game.shapeRenderer.setColor(skin.outlineColor)
            game.shapeRenderer.rect(x, y, skinBoxSize, 3f)
            game.shapeRenderer.rect(x, y + skinBoxSize - 3, skinBoxSize, 3f)
            game.shapeRenderer.rect(x, y, 3f, skinBoxSize)
            game.shapeRenderer.rect(x + skinBoxSize - 3, y, 3f, skinBoxSize)

            // Highlight
            game.shapeRenderer.setColor(skin.highlightColor)
            game.shapeRenderer.rect(x + 10, y + skinBoxSize - 15, skinBoxSize - 20, 5f)
        }

        game.shapeRenderer.end()

        // Draw UI text
        game.batch.projectionMatrix = camera.combined
        game.batch.begin()

        // Title
        game.font.data.setScale(3f)
        game.font.color = Constants.UI_SHADOW_COLOR
        game.font.draw(game.batch, "SELECT SKIN", 2f, JumperGame.GAME_HEIGHT - 48f, JumperGame.GAME_WIDTH, Align.center, false)
        game.font.color = Constants.UI_TEXT_COLOR
        game.font.draw(game.batch, "SELECT SKIN", 0f, JumperGame.GAME_HEIGHT - 50f, JumperGame.GAME_WIDTH, Align.center, false)

        // Skin names
        skins.forEachIndexed { index, skin ->
            val col = index % skinsPerRow
            val row = index / skinsPerRow
            val x = startX + col * (skinBoxSize + 20f)
            val y = startY - row * (skinBoxSize + 40f) - 15f

            game.font.data.setScale(1.5f)
            game.font.color = Constants.UI_TEXT_COLOR
            game.font.draw(game.batch, skin.displayName, x, y, skinBoxSize, Align.center, false)
        }

        // Back button
        game.font.data.setScale(2f)
        game.font.color = Constants.UI_SHADOW_COLOR
        game.font.draw(game.batch, "TAP HERE TO GO BACK", 2f, 48f, JumperGame.GAME_WIDTH, Align.center, false)
        game.font.color = Constants.UI_TEXT_COLOR
        game.font.draw(game.batch, "TAP HERE TO GO BACK", 0f, 50f, JumperGame.GAME_WIDTH, Align.center, false)

        game.batch.end()

        // Handle input
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = (Gdx.graphics.height - Gdx.input.y).toFloat()
            val screenWidth = Gdx.graphics.width.toFloat()
            val screenHeight = Gdx.graphics.height.toFloat()

            val gameX = (touchX / screenWidth) * JumperGame.GAME_WIDTH
            val gameY = (touchY / screenHeight) * JumperGame.GAME_HEIGHT

            // Check back button
            if (gameY < 100f) {
                game.screen = MenuScreen(game)
                dispose()
                return
            }

            // Check skin selection
            skins.forEachIndexed { index, skin ->
                val col = index % skinsPerRow
                val row = index / skinsPerRow
                val x = startX + col * (skinBoxSize + 20f)
                val y = startY - row * (skinBoxSize + 40f)

                if (gameX >= x && gameX <= x + skinBoxSize &&
                    gameY >= y && gameY <= y + skinBoxSize) {
                    SkinManager.setCurrentSkin(skin)
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