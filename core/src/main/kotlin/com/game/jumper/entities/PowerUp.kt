package com.game.jumper.entities

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.game.jumper.utils.Constants

/**
 * PowerUp entity - collectible items that drop from destroyed obstacles
 */
class PowerUp(x: Float, y: Float, val type: PowerUpType) {

    private val bounds = Rectangle(x, y, Constants.POWERUP_SIZE, Constants.POWERUP_SIZE)
    private var animationTime = 0f
    private var pulseScale = 1f

    fun update(delta: Float) {
        // Fall down slowly
        bounds.y -= Constants.POWERUP_FALL_SPEED * delta

        // Pulsing animation
        animationTime += delta * 3f
        pulseScale = 1f + kotlin.math.sin(animationTime) * 0.15f
    }

    /**
     * Check if power-up is off screen (below ground)
     */
    fun isOffScreen(): Boolean {
        return bounds.y + bounds.height < Constants.GROUND_HEIGHT
    }

    /**
     * Check collision with player
     */
    fun collidesWith(player: Player): Boolean {
        return bounds.overlaps(player.getBounds())
    }

    /**
     * Render the power-up with pulsing animation
     */
    fun render(shapeRenderer: ShapeRenderer) {
        val color = when (type) {
            PowerUpType.WEAPON_RAPID_FIRE, PowerUpType.WEAPON_LASER, PowerUpType.WEAPON_SPREAD ->
                Constants.POWERUP_WEAPON_COLOR
            PowerUpType.POWER_JUMP ->
                Constants.POWERUP_JUMP_COLOR
        }

        // Calculate pulsed size
        val centerX = bounds.x + bounds.width / 2
        val centerY = bounds.y + bounds.height / 2
        val pulsedSize = bounds.width * pulseScale

        // Glow effect
        shapeRenderer.setColor(color.r, color.g, color.b, 0.3f)
        shapeRenderer.rect(centerX - pulsedSize / 2 - 3, centerY - pulsedSize / 2 - 3, pulsedSize + 6, pulsedSize + 6)

        // Main body
        shapeRenderer.setColor(color)
        shapeRenderer.rect(centerX - pulsedSize / 2, centerY - pulsedSize / 2, pulsedSize, pulsedSize)

        // Bright center
        val innerSize = pulsedSize * 0.5f
        shapeRenderer.setColor(1f, 1f, 1f, 0.9f)
        shapeRenderer.rect(centerX - innerSize / 2, centerY - innerSize / 2, innerSize, innerSize)
    }

    fun renderBorder(shapeRenderer: ShapeRenderer) {
        val centerX = bounds.x + bounds.width / 2
        val centerY = bounds.y + bounds.height / 2
        val pulsedSize = bounds.width * pulseScale

        shapeRenderer.setColor(1f, 1f, 1f, 1f) // White border
        shapeRenderer.rect(centerX - pulsedSize / 2, centerY - pulsedSize / 2, pulsedSize, pulsedSize)
    }

    fun getBounds(): Rectangle = bounds
}