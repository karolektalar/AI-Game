package com.game.jumper.entities

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.game.jumper.utils.Constants

/**
 * PowerUp entity - collectible items that drop from destroyed obstacles
 */
class PowerUp(x: Float, y: Float, val type: PowerUpType) {

    private val bounds = Rectangle(x, y, Constants.POWERUP_SIZE, Constants.POWERUP_SIZE)

    fun update(delta: Float) {
        // Fall down slowly
        bounds.y -= Constants.POWERUP_FALL_SPEED * delta
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
     * Render the power-up as a colored square with border
     */
    fun render(shapeRenderer: ShapeRenderer) {
        val color = when (type) {
            PowerUpType.WEAPON_RAPID_FIRE, PowerUpType.WEAPON_LASER, PowerUpType.WEAPON_SPREAD ->
                Constants.POWERUP_WEAPON_COLOR
            PowerUpType.POWER_JUMP ->
                Constants.POWERUP_JUMP_COLOR
        }
        shapeRenderer.setColor(color)
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height)
    }

    fun renderBorder(shapeRenderer: ShapeRenderer) {
        shapeRenderer.setColor(1f, 1f, 1f, 1f) // White border
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height)
    }

    fun getBounds(): Rectangle = bounds
}