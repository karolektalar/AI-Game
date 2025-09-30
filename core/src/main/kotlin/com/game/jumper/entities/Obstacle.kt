package com.game.jumper.entities

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.game.jumper.utils.Constants

/**
 * Obstacle entity - represents moving obstacles that player must avoid
 */
class Obstacle(x: Float, y: Float, private val speed: Float = Constants.OBSTACLE_SPEED) {

    private val bounds = Rectangle(x, y, Constants.OBSTACLE_WIDTH, Constants.OBSTACLE_HEIGHT)
    private var scored = false // Track if this obstacle has been scored

    fun update(delta: Float) {
        // Move obstacle from right to left
        bounds.x -= speed * delta
    }

    /**
     * Check if obstacle is off screen (left side)
     */
    fun isOffScreen(): Boolean {
        return bounds.x + bounds.width < 0
    }

    /**
     * Check if player has passed this obstacle
     */
    fun isPassedBy(playerX: Float): Boolean {
        return bounds.x + bounds.width < playerX
    }

    /**
     * Mark this obstacle as scored
     */
    fun markAsScored() {
        scored = true
    }

    /**
     * Check if this obstacle has been scored
     */
    fun hasBeenScored(): Boolean = scored

    /**
     * Render the obstacle as a colored rectangle
     */
    fun render(shapeRenderer: ShapeRenderer) {
        shapeRenderer.setColor(Constants.OBSTACLE_COLOR)
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height)
    }

    fun getBounds(): Rectangle = bounds

    fun getX(): Float = bounds.x
}