package com.game.jumper.entities

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.game.jumper.utils.Constants

/**
 * Enemy entity that moves left and shoots bullets to the left
 */
class Enemy(x: Float, y: Float) {
    private val bounds = Rectangle(x, y, Constants.ENEMY_WIDTH, Constants.ENEMY_HEIGHT)
    private var shootTimer = 0f
    private var scored = false
    private var lives = 2 // Enemies have 2 lives

    fun update(delta: Float) {
        // Move left
        bounds.x -= Constants.ENEMY_SPEED * delta

        // Update shoot timer
        shootTimer += delta
    }

    fun render(renderer: ShapeRenderer) {
        // Main body (hexagon-like shape)
        renderer.color = Constants.ENEMY_COLOR

        val centerX = bounds.x + bounds.width / 2
        val centerY = bounds.y + bounds.height / 2
        val radiusX = bounds.width / 2
        val radiusY = bounds.height / 2

        // Draw enemy as a diamond/rhombus shape
        renderer.triangle(
            centerX, centerY + radiusY, // Top
            centerX - radiusX, centerY, // Left
            centerX, centerY - radiusY  // Bottom
        )
        renderer.triangle(
            centerX, centerY + radiusY, // Top
            centerX + radiusX, centerY, // Right
            centerX, centerY - radiusY  // Bottom
        )

        // Add "eye" in center
        renderer.color = Constants.ENEMY_OUTLINE
        renderer.circle(centerX, centerY, radiusX * 0.3f, 12)
    }

    fun renderOutline(renderer: ShapeRenderer) {
        renderer.color = Constants.ENEMY_OUTLINE
        val centerX = bounds.x + bounds.width / 2
        val centerY = bounds.y + bounds.height / 2
        val radiusX = bounds.width / 2
        val radiusY = bounds.height / 2

        // Outline
        renderer.rectLine(centerX, centerY + radiusY, centerX - radiusX, centerY, 2f)
        renderer.rectLine(centerX - radiusX, centerY, centerX, centerY - radiusY, 2f)
        renderer.rectLine(centerX, centerY - radiusY, centerX + radiusX, centerY, 2f)
        renderer.rectLine(centerX + radiusX, centerY, centerX, centerY + radiusY, 2f)
    }

    fun canShoot(): Boolean {
        return shootTimer >= Constants.ENEMY_SHOOT_INTERVAL
    }

    fun shoot(): EnemyBullet? {
        if (canShoot()) {
            shootTimer = 0f
            // Shoot from left side of enemy (towards left)
            return EnemyBullet(bounds.x, bounds.y + bounds.height / 2 - Constants.ENEMY_BULLET_HEIGHT / 2)
        }
        return null
    }

    fun takeDamage() {
        lives--
    }

    fun isDead(): Boolean = lives <= 0

    fun isOffScreen(screenWidth: Float): Boolean {
        return bounds.x + bounds.width < 0
    }

    fun hasBeenScored(): Boolean = scored

    fun markAsScored() {
        scored = true
    }

    fun isPassedBy(playerX: Float): Boolean {
        return bounds.x + bounds.width < playerX
    }

    fun getBounds(): Rectangle = bounds
}
