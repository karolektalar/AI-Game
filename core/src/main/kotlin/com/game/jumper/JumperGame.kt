package com.game.jumper

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.game.jumper.screens.MenuScreen
import com.game.jumper.managers.AdManager

/**
 * Main game class - entry point for the LibGDX application
 * Manages screens and provides shared resources to all game screens
 */
class JumperGame(private val adManager: AdManager) : Game() {

    lateinit var batch: SpriteBatch
        private set

    lateinit var shapeRenderer: ShapeRenderer
        private set

    lateinit var font: BitmapFont
        private set

    companion object {
        const val GAME_WIDTH = 480f
        const val GAME_HEIGHT = 800f
        const val GAME_NAME = "Jumper Game"
    }

    override fun create() {
        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        font = BitmapFont().apply {
            data.setScale(2f)
        }

        // Initialize managers
        com.game.jumper.managers.GameStateManager.init()
        com.game.jumper.managers.SkinManager.init()

        // Start with the main menu
        setScreen(MenuScreen(this))
    }

    fun getAdManager(): AdManager = adManager

    override fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
        font.dispose()
        screen?.dispose()
    }
}