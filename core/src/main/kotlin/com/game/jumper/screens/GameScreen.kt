package com.game.jumper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import com.game.jumper.JumperGame
import com.game.jumper.entities.*
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
    private val bullets = mutableListOf<Bullet>()
    private val powerUps = mutableListOf<PowerUp>()

    private var spawnTimer = 0f
    private var shootCooldown = 0f
    private var gameOver = false

    override fun show() {
        // Reset game state
        GameStateManager.resetCurrentScore()
        obstacles.clear()
        bullets.clear()
        powerUps.clear()
        player.reset()
        spawnTimer = 0f
        shootCooldown = 0f
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

        for (bullet in bullets) {
            bullet.render(game.shapeRenderer)
        }

        for (powerUp in powerUps) {
            powerUp.render(game.shapeRenderer)
        }

        game.shapeRenderer.end()

        // Render power-up borders
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        for (powerUp in powerUps) {
            powerUp.renderBorder(game.shapeRenderer)
        }
        game.shapeRenderer.end()

        // Render UI
        game.batch.projectionMatrix = camera.combined
        game.batch.begin()

        // Score and weapon info
        val scoreText = "SCORE: ${GameStateManager.getCurrentScore()}"
        game.font.draw(
            game.batch,
            scoreText,
            10f,
            JumperGame.GAME_HEIGHT - 20f
        )

        // Current weapon
        val weaponText = "WEAPON: ${player.getCurrentWeapon().name}"
        game.font.draw(
            game.batch,
            weaponText,
            10f,
            JumperGame.GAME_HEIGHT - 50f
        )

        // Power jump indicator
        if (player.hasPowerJump()) {
            val powerJumpText = "POWER JUMP!"
            game.font.draw(
                game.batch,
                powerJumpText,
                10f,
                JumperGame.GAME_HEIGHT - 80f
            )
        }

        // Debug info
        val debugText = "PowerUps: ${powerUps.size} | Obstacles: ${obstacles.size}"
        game.font.draw(
            game.batch,
            debugText,
            10f,
            JumperGame.GAME_HEIGHT - 110f
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
                // Convert screen coordinates to game coordinates
                val touchX = Gdx.input.x.toFloat()
                val touchY = (Gdx.graphics.height - Gdx.input.y).toFloat() // Flip Y coordinate
                val screenWidth = Gdx.graphics.width.toFloat()
                val screenHeight = Gdx.graphics.height.toFloat()

                // Map screen coordinates to game coordinates
                val gameX = (touchX / screenWidth) * JumperGame.GAME_WIDTH
                val gameY = (touchY / screenHeight) * JumperGame.GAME_HEIGHT

                // Check if clicked on a power-up
                var powerUpClicked = false
                val powerUpIterator = powerUps.iterator()
                while (powerUpIterator.hasNext()) {
                    val powerUp = powerUpIterator.next()
                    val bounds = powerUp.getBounds()
                    if (gameX >= bounds.x && gameX <= bounds.x + bounds.width &&
                        gameY >= bounds.y && gameY <= bounds.y + bounds.height) {
                        applyPowerUp(powerUp.type)
                        powerUpIterator.remove()
                        powerUpClicked = true
                        break
                    }
                }

                // If no power-up was clicked, handle jump/shoot
                if (!powerUpClicked) {
                    if (touchX < screenWidth / 2) {
                        // Left side - jump
                        player.jump()
                    } else {
                        // Right side - shoot (with cooldown)
                        if (shootCooldown <= 0f) {
                            bullets.addAll(player.shoot())
                            shootCooldown = Constants.SHOOT_COOLDOWN
                        }
                    }
                }
            }
        }
    }

    private fun update(delta: Float) {
        // Update player
        player.update(delta)

        // Check if player fell to the ground (game over)
        if (player.isOnGround()) {
            gameOver = true
        }

        // Update shoot cooldown
        if (shootCooldown > 0f) {
            shootCooldown -= delta
        }

        // Spawn obstacles
        spawnTimer += delta
        if (spawnTimer >= Constants.OBSTACLE_SPAWN_INTERVAL) {
            spawnObstacle()
            spawnTimer = 0f
        }

        // Update bullets
        val bulletIterator = bullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            bullet.update(delta)

            // Remove off-screen bullets
            if (bullet.isOffScreen(JumperGame.GAME_WIDTH)) {
                bulletIterator.remove()
            }
        }

        // Update power-ups
        val powerUpIterator = powerUps.iterator()
        while (powerUpIterator.hasNext()) {
            val powerUp = powerUpIterator.next()
            powerUp.update(delta)

            // Check collision with player
            if (powerUp.collidesWith(player)) {
                applyPowerUp(powerUp.type)
                powerUpIterator.remove()
                continue
            }

            // Remove off-screen power-ups
            if (powerUp.isOffScreen()) {
                powerUpIterator.remove()
            }
        }

        // Update obstacles
        val obstacleIterator = obstacles.iterator()
        while (obstacleIterator.hasNext()) {
            val obstacle = obstacleIterator.next()
            obstacle.update(delta)

            // Check collision with player
            if (player.collidesWith(obstacle)) {
                gameOver = true
            }

            // Check collision with bullets
            var obstacleHit = false
            val bulletCheckIterator = bullets.iterator()
            while (bulletCheckIterator.hasNext()) {
                val bullet = bulletCheckIterator.next()
                if (bullet.collidesWith(obstacle)) {
                    bulletCheckIterator.remove()
                    obstacleHit = true
                    break
                }
            }

            // Remove obstacle if hit by bullet and possibly drop power-up
            if (obstacleHit) {
                // Random chance to drop power-up
                if (Random.nextFloat() < Constants.POWERUP_DROP_CHANCE) {
                    val obstacleBounds = obstacle.getBounds()
                    // Spawn at center of obstacle
                    val powerUpX = obstacleBounds.x + obstacleBounds.width / 2 - Constants.POWERUP_SIZE / 2
                    val powerUpY = obstacleBounds.y + obstacleBounds.height / 2 - Constants.POWERUP_SIZE / 2
                    spawnPowerUp(powerUpX, powerUpY)
                }
                obstacleIterator.remove()
                continue
            }

            // Check scoring
            if (!obstacle.hasBeenScored() && obstacle.isPassedBy(player.getX())) {
                obstacle.markAsScored()
                GameStateManager.incrementScore()
            }

            // Remove off-screen obstacles
            if (obstacle.isOffScreen()) {
                obstacleIterator.remove()
            }
        }
    }

    private fun applyPowerUp(type: PowerUpType) {
        when (type) {
            PowerUpType.WEAPON_RAPID_FIRE -> player.setWeapon(WeaponType.RAPID_FIRE)
            PowerUpType.WEAPON_LASER -> player.setWeapon(WeaponType.LASER)
            PowerUpType.WEAPON_SPREAD -> player.setWeapon(WeaponType.SPREAD)
            PowerUpType.POWER_JUMP -> player.setPowerJump(true)
        }
    }

    private fun spawnPowerUp(x: Float, y: Float) {
        // Random power-up type
        val types = PowerUpType.values()
        val randomType = types[Random.nextInt(types.size)]
        powerUps.add(PowerUp(x, y, randomType))
    }

    private fun spawnObstacle() {
        // Random size for obstacle
        val width = Random.nextFloat() * (Constants.OBSTACLE_MAX_WIDTH - Constants.OBSTACLE_MIN_WIDTH) + Constants.OBSTACLE_MIN_WIDTH
        val height = Random.nextFloat() * (Constants.OBSTACLE_MAX_HEIGHT - Constants.OBSTACLE_MIN_HEIGHT) + Constants.OBSTACLE_MIN_HEIGHT

        // Random shape
        val shapes = ObstacleShape.values()
        val randomShape = shapes[Random.nextInt(shapes.size)]

        // Random height within valid range
        val minY = Constants.GROUND_HEIGHT
        val maxY = JumperGame.GAME_HEIGHT - height - 100f
        val randomY = Random.nextFloat() * (maxY - minY) + minY

        val obstacle = Obstacle(JumperGame.GAME_WIDTH, randomY, width, height, randomShape)
        obstacles.add(obstacle)
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}