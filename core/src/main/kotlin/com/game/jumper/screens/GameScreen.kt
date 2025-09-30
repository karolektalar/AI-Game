package com.game.jumper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import com.game.jumper.JumperGame
import com.game.jumper.entities.Ground
import com.game.jumper.entities.Obstacle
import com.game.jumper.entities.Player
import com.game.jumper.managers.GameStateManager
import com.game.jumper.utils.Constants
import kotlin.random.Random

/**
 * Main game screen - where the actual gameplay happens
 * Player taps to jump and avoid obstacles
 */
class GameScreen(private val game: JumperGame) : Screen {

    private val camera = OrthographicCamera().apply {
        setToOrtho(false, JumperGame.GAME_WIDTH, JumperGame.GAME_HEIGHT)
    }

    private val player = Player(Constants.PLAYER_START_X, Constants.PLAYER_START_Y)
    private val ground = Ground()
    private val obstacles = mutableListOf<Obstacle>()

    private var spawnTimer = 0f
    private var gameOver = false

    override fun show() {
        // Reset game state
        GameStateManager.resetCurrentScore()
        obstacles.clear()
        player.reset()
        spawnTimer = 0f
        gameOver = false
    }

    override fun render(delta: Float) {
        if (!gameOver) {
            update(delta)
        }

        // Clear screen
        Gdx.gl.glClearColor(
            Constants.BACKGROUND_COLOR.r,
            Constants.BACKGROUND_COLOR.g,
            Constants.BACKGROUND_COLOR.b,
            1f
        )
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()

        // Render game objects
        game.shapeRenderer.projectionMatrix = camera.combined
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        ground.render(game.shapeRenderer)
        player.render(game.shapeRenderer)

        for (obstacle in obstacles) {
            obstacle.render(game.shapeRenderer)
        }

        game.shapeRenderer.end()

        // Render UI
        game.batch.projectionMatrix = camera.combined
        game.batch.begin()

        // Score
        val scoreText = "SCORE: ${GameStateManager.getCurrentScore()}"
        game.font.draw(
            game.batch,
            scoreText,
            10f,
            JumperGame.GAME_HEIGHT - 20f
        )

        // Game over message
        if (gameOver) {
            val gameOverText = "GAME OVER"
            game.font.draw(
                game.batch,
                gameOverText,
                0f,
                JumperGame.GAME_HEIGHT / 2f + 50f,
                JumperGame.GAME_WIDTH,
                Align.center,
                false
            )

            val tapText = "TAP TO CONTINUE"
            game.font.draw(
                game.batch,
                tapText,
                0f,
                JumperGame.GAME_HEIGHT / 2f - 50f,
                JumperGame.GAME_WIDTH,
                Align.center,
                false
            )
        }

        game.batch.end()

        // Handle input
        if (Gdx.input.justTouched()) {
            if (gameOver) {
                game.screen = GameOverScreen(game)
                dispose()
            } else {
                player.jump()
            }
        }
    }

    private fun update(delta: Float) {
        // Update player
        player.update(delta)

        // Spawn obstacles
        spawnTimer += delta
        if (spawnTimer >= Constants.OBSTACLE_SPAWN_INTERVAL) {
            spawnObstacle()
            spawnTimer = 0f
        }

        // Update obstacles
        val iterator = obstacles.iterator()
        while (iterator.hasNext()) {
            val obstacle = iterator.next()
            obstacle.update(delta)

            // Check collision
            if (player.collidesWith(obstacle)) {
                gameOver = true
            }

            // Check scoring
            if (!obstacle.hasBeenScored() && obstacle.isPassedBy(player.getX())) {
                obstacle.markAsScored()
                GameStateManager.incrementScore()
            }

            // Remove off-screen obstacles
            if (obstacle.isOffScreen()) {
                iterator.remove()
            }
        }
    }

    private fun spawnObstacle() {
        // Random height within valid range
        val minY = Constants.GROUND_HEIGHT
        val maxY = JumperGame.GAME_HEIGHT - Constants.OBSTACLE_HEIGHT - 100f
        val randomY = Random.nextFloat() * (maxY - minY) + minY

        val obstacle = Obstacle(JumperGame.GAME_WIDTH, randomY)
        obstacles.add(obstacle)
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}