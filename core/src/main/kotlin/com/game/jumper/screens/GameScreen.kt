package com.game.jumper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import com.game.jumper.JumperGame
import com.game.jumper.effects.ParticleSystem
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
    private val enemies = mutableListOf<Enemy>()
    private val enemyBullets = mutableListOf<EnemyBullet>()
    private val lifePickups = mutableListOf<LifePickup>()
    private val particleSystem = ParticleSystem()

    private var spawnTimer = 0f
    private var enemySpawnTimer = 0f
    private var shootCooldown = 0f
    private var gameOver = false

    override fun show() {
        // Reset game state
        GameStateManager.resetCurrentScore()
        obstacles.clear()
        bullets.clear()
        powerUps.clear()
        enemies.clear()
        enemyBullets.clear()
        lifePickups.clear()
        particleSystem.clear()
        player.reset()

        // Apply current skin
        player.setSkin(com.game.jumper.managers.SkinManager.getCurrentSkin())

        spawnTimer = 0f
        enemySpawnTimer = 0f
        shootCooldown = 0f
        gameOver = false
    }

    override fun render(delta: Float) {
        if (!gameOver) {
            update(delta)
        }

        // Clear screen with gradient background
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()

        // Render gradient background
        game.shapeRenderer.projectionMatrix = camera.combined
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // Draw gradient from top to bottom
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

        // Render game objects
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

        for (enemy in enemies) {
            enemy.render(game.shapeRenderer)
        }

        for (enemyBullet in enemyBullets) {
            enemyBullet.render(game.shapeRenderer)
        }

        for (lifePickup in lifePickups) {
            lifePickup.render(game.shapeRenderer)
        }

        // Render particles
        particleSystem.render(game.shapeRenderer)

        game.shapeRenderer.end()

        // Render power-up borders and enemy outlines
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        for (powerUp in powerUps) {
            powerUp.renderBorder(game.shapeRenderer)
        }
        for (enemy in enemies) {
            enemy.renderOutline(game.shapeRenderer)
        }
        for (lifePickup in lifePickups) {
            lifePickup.renderOutline(game.shapeRenderer)
        }
        game.shapeRenderer.end()

        // Render UI
        game.batch.projectionMatrix = camera.combined
        game.batch.begin()

        // Helper function to draw text with shadow
        fun drawTextWithShadow(text: String, x: Float, y: Float, scale: Float = 2f) {
            game.font.data.setScale(scale)
            // Shadow
            game.font.color = Constants.UI_SHADOW_COLOR
            game.font.draw(game.batch, text, x + 2, y - 2)
            // Text
            game.font.color = Constants.UI_TEXT_COLOR
            game.font.draw(game.batch, text, x, y)
        }

        // Lives (top-left)
        drawTextWithShadow("LIVES: ${player.getLives()}", 10f, JumperGame.GAME_HEIGHT - 20f, 2.5f)

        // Score
        drawTextWithShadow("SCORE: ${GameStateManager.getCurrentScore()}", 10f, JumperGame.GAME_HEIGHT - 55f, 2.5f)

        // Current weapon
        drawTextWithShadow("WEAPON: ${player.getCurrentWeapon().name}", 10f, JumperGame.GAME_HEIGHT - 90f, 1.8f)

        // Power jump indicator (glowing effect)
        if (player.hasPowerJump()) {
            game.font.data.setScale(2f)
            game.font.color = com.badlogic.gdx.graphics.Color.CYAN
            game.font.draw(game.batch, "POWER JUMP ACTIVE!", 10f, JumperGame.GAME_HEIGHT - 125f)
        }

        // Debug info (smaller)
        drawTextWithShadow("Enemies: ${enemies.size} | Obstacles: ${obstacles.size}", 10f, JumperGame.GAME_HEIGHT - 155f, 1.3f)

        // Game over message
        if (gameOver) {
            // Semi-transparent overlay
            game.shapeRenderer.projectionMatrix = camera.combined
            game.batch.end()
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            game.shapeRenderer.setColor(0f, 0f, 0f, 0.7f)
            game.shapeRenderer.rect(0f, 0f, JumperGame.GAME_WIDTH, JumperGame.GAME_HEIGHT)
            game.shapeRenderer.end()
            game.batch.begin()

            // Game Over text with shadow
            game.font.data.setScale(4f)
            val gameOverText = "GAME OVER"
            // Red shadow
            game.font.color = com.badlogic.gdx.graphics.Color.RED
            game.font.draw(
                game.batch,
                gameOverText,
                5f,
                JumperGame.GAME_HEIGHT / 2f + 55f,
                JumperGame.GAME_WIDTH,
                Align.center,
                false
            )
            // White text
            game.font.color = com.badlogic.gdx.graphics.Color.WHITE
            game.font.draw(
                game.batch,
                gameOverText,
                0f,
                JumperGame.GAME_HEIGHT / 2f + 50f,
                JumperGame.GAME_WIDTH,
                Align.center,
                false
            )

            // Tap to continue (smaller, pulsing)
            game.font.data.setScale(2f)
            val alpha = 0.5f + kotlin.math.sin(System.currentTimeMillis() * 0.003f) * 0.5f
            game.font.color = com.badlogic.gdx.graphics.Color(1f, 1f, 1f, alpha)
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

                            // Add shoot particle effect
                            particleSystem.addShootEffect(
                                player.getX() + Constants.PLAYER_WIDTH,
                                player.getY() + Constants.PLAYER_HEIGHT / 2,
                                Constants.BULLET_COLOR
                            )
                        }
                    }
                }
            }
        }
    }

    private fun update(delta: Float) {
        // Update player
        player.update(delta)

        // Update particles
        particleSystem.update(delta)

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

        // Spawn enemies
        enemySpawnTimer += delta
        if (enemySpawnTimer >= Constants.ENEMY_SPAWN_INTERVAL) {
            spawnEnemy()
            enemySpawnTimer = 0f
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

            // Check collision with player - takes damage
            if (player.collidesWith(obstacle)) {
                val alive = player.takeDamage()
                if (!alive) {
                    gameOver = true
                }
                // Add damage particle effect
                particleSystem.addExplosion(
                    player.getX() + Constants.PLAYER_WIDTH / 2,
                    player.getY() + Constants.PLAYER_HEIGHT / 2,
                    Constants.OBSTACLE_COLOR_1,
                    15
                )
                obstacleIterator.remove()
                continue
            }

            // Check collision with bullets
            var obstacleHit = false
            val bulletCheckIterator = bullets.iterator()
            while (bulletCheckIterator.hasNext()) {
                val bullet = bulletCheckIterator.next()
                if (bullet.collidesWith(obstacle)) {
                    bulletCheckIterator.remove()
                    obstacleHit = true

                    // Add explosion particle effect
                    val obstacleBounds = obstacle.getBounds()
                    val explosionX = obstacleBounds.x + obstacleBounds.width / 2
                    val explosionY = obstacleBounds.y + obstacleBounds.height / 2
                    particleSystem.addExplosion(explosionX, explosionY, Constants.OBSTACLE_COLOR_1, 30)
                    break
                }
            }

            // Remove obstacle if hit by bullet and possibly drop power-up or life
            if (obstacleHit) {
                val obstacleBounds = obstacle.getBounds()
                val centerX = obstacleBounds.x + obstacleBounds.width / 2
                val centerY = obstacleBounds.y + obstacleBounds.height / 2

                // Random chance to drop power-up
                if (Random.nextFloat() < Constants.POWERUP_DROP_CHANCE) {
                    val powerUpX = centerX - Constants.POWERUP_SIZE / 2
                    val powerUpY = centerY - Constants.POWERUP_SIZE / 2
                    spawnPowerUp(powerUpX, powerUpY)
                }
                // Random chance to drop life
                else if (Random.nextFloat() < Constants.LIFE_DROP_CHANCE) {
                    val lifeX = centerX - Constants.LIFE_PICKUP_SIZE / 2
                    val lifeY = centerY - Constants.LIFE_PICKUP_SIZE / 2
                    lifePickups.add(LifePickup(lifeX, lifeY))
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

        // Update life pickups
        val lifeIterator = lifePickups.iterator()
        while (lifeIterator.hasNext()) {
            val lifePickup = lifeIterator.next()
            lifePickup.update(delta)

            // Check collision with player
            if (lifePickup.collidesWith(player)) {
                player.heal()
                lifeIterator.remove()
                continue
            }

            // Remove off-screen life pickups
            if (lifePickup.isOffScreen()) {
                lifeIterator.remove()
            }
        }

        // Update enemies
        val enemyIterator = enemies.iterator()
        while (enemyIterator.hasNext()) {
            val enemy = enemyIterator.next()
            enemy.update(delta)

            // Enemy shoots
            val enemyBullet = enemy.shoot()
            if (enemyBullet != null) {
                enemyBullets.add(enemyBullet)
            }

            // Check collision with player bullets
            var enemyHit = false
            val bulletCheckIterator = bullets.iterator()
            while (bulletCheckIterator.hasNext()) {
                val bullet = bulletCheckIterator.next()
                if (bullet.collidesWith(enemy.getBounds())) {
                    bulletCheckIterator.remove()
                    enemy.takeDamage()
                    enemyHit = true

                    // Add explosion particle effect
                    val enemyBounds = enemy.getBounds()
                    val explosionX = enemyBounds.x + enemyBounds.width / 2
                    val explosionY = enemyBounds.y + enemyBounds.height / 2
                    particleSystem.addExplosion(explosionX, explosionY, Constants.ENEMY_COLOR, 20)
                    break
                }
            }

            // Remove enemy if dead and possibly drop life
            if (enemy.isDead()) {
                val enemyBounds = enemy.getBounds()
                val centerX = enemyBounds.x + enemyBounds.width / 2
                val centerY = enemyBounds.y + enemyBounds.height / 2

                // Drop life
                if (Random.nextFloat() < Constants.LIFE_DROP_CHANCE) {
                    val lifeX = centerX - Constants.LIFE_PICKUP_SIZE / 2
                    val lifeY = centerY - Constants.LIFE_PICKUP_SIZE / 2
                    lifePickups.add(LifePickup(lifeX, lifeY))
                }

                enemyIterator.remove()
                continue
            }

            // Check scoring
            if (!enemy.hasBeenScored() && enemy.isPassedBy(player.getX())) {
                enemy.markAsScored()
                GameStateManager.incrementScore()
            }

            // Remove off-screen enemies
            if (enemy.isOffScreen()) {
                enemyIterator.remove()
            }
        }

        // Update enemy bullets
        val enemyBulletIterator = enemyBullets.iterator()
        while (enemyBulletIterator.hasNext()) {
            val enemyBullet = enemyBulletIterator.next()
            enemyBullet.update(delta)

            // Check collision with player - takes damage
            if (enemyBullet.collidesWith(player)) {
                val alive = player.takeDamage()
                if (!alive) {
                    gameOver = true
                }
                // Add damage particle effect
                particleSystem.addExplosion(
                    player.getX() + Constants.PLAYER_WIDTH / 2,
                    player.getY() + Constants.PLAYER_HEIGHT / 2,
                    Constants.ENEMY_BULLET_COLOR,
                    15
                )
                enemyBulletIterator.remove()
                continue
            }

            // Remove off-screen bullets
            if (enemyBullet.isOffScreen()) {
                enemyBulletIterator.remove()
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

    private fun spawnEnemy() {
        // Random height within valid range
        val minY = Constants.GROUND_HEIGHT
        val maxY = JumperGame.GAME_HEIGHT - Constants.ENEMY_HEIGHT - 100f
        val randomY = Random.nextFloat() * (maxY - minY) + minY

        val enemy = Enemy(JumperGame.GAME_WIDTH, randomY)
        enemies.add(enemy)
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}