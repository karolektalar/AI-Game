package com.game.jumper.entities

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.game.jumper.utils.Constants
import kotlin.math.sin

/**
 * Life pickup that falls from destroyed obstacles
 * Heals player when collected
 */
class LifePickup(x: Float, y: Float) {
    private val bounds = Rectangle(x, y, Constants.LIFE_PICKUP_SIZE, Constants.LIFE_PICKUP_SIZE)
    private var animationTime = 0f
    private var pulseScale = 1f

    fun update(delta: Float) {
        // Fall down
        bounds.y -= Constants.LIFE_PICKUP_FALL_SPEED * delta

        // Pulsing animation
        animationTime += delta * 3f
        pulseScale = 1f + sin(animationTime) * 0.15f
    }

    fun render(renderer: ShapeRenderer) {
        val centerX = bounds.x + bounds.width / 2
        val centerY = bounds.y + bounds.height / 2
        val size = bounds.width * pulseScale

        // Draw heart shape (simplified as two circles and a triangle)
        renderer.color = Constants.LIFE_COLOR

        // Left circle
        renderer.circle(centerX - size * 0.2f, centerY + size * 0.15f, size * 0.3f, 20)
        // Right circle
        renderer.circle(centerX + size * 0.2f, centerY + size * 0.15f, size * 0.3f, 20)

        // Bottom triangle
        renderer.triangle(
            centerX - size * 0.4f, centerY + size * 0.1f,
            centerX + size * 0.4f, centerY + size * 0.1f,
            centerX, centerY - size * 0.4f
        )
    }

    fun renderOutline(renderer: ShapeRenderer) {
        val centerX = bounds.x + bounds.width / 2
        val centerY = bounds.y + bounds.height / 2
        val size = bounds.width * pulseScale

        renderer.color = Constants.LIFE_OUTLINE

        // Outline circles
        renderer.circle(centerX - size * 0.2f, centerY + size * 0.15f, size * 0.3f, 20)
        renderer.circle(centerX + size * 0.2f, centerY + size * 0.15f, size * 0.3f, 20)
    }

    fun collidesWith(player: Player): Boolean {
        return bounds.overlaps(player.getBounds())
    }

    fun isOffScreen(): Boolean {
        return bounds.y + bounds.height < 0
    }

    fun getBounds(): Rectangle = bounds
}
