package com.game.jumper.entities

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.game.jumper.utils.Constants

/**
 * Obstacle entity - represents moving obstacles that player must avoid
 * All collision detection uses rectangle bounds regardless of visual shape
 */
class Obstacle(
    x: Float,
    y: Float,
    private val width: Float,
    private val height: Float,
    val shape: ObstacleShape = ObstacleShape.RECTANGLE,
    private val speed: Float = Constants.OBSTACLE_SPEED
) {

    private val bounds = Rectangle(x, y, width, height)
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
     * Render the obstacle based on its shape with enhanced visuals
     */
    fun render(shapeRenderer: ShapeRenderer) {
        when (shape) {
            ObstacleShape.RECTANGLE -> {
                // Main body
                shapeRenderer.setColor(Constants.OBSTACLE_COLOR_1)
                shapeRenderer.rect(bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4)

                // Darker shade for depth
                shapeRenderer.setColor(Constants.OBSTACLE_COLOR_2)
                shapeRenderer.rect(bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height / 2 - 2)

                // Outline
                shapeRenderer.setColor(Constants.OBSTACLE_OUTLINE)
                shapeRenderer.rect(bounds.x, bounds.y, bounds.width, 2f)
                shapeRenderer.rect(bounds.x, bounds.y + bounds.height - 2, bounds.width, 2f)
                shapeRenderer.rect(bounds.x, bounds.y, 2f, bounds.height)
                shapeRenderer.rect(bounds.x + bounds.width - 2, bounds.y, 2f, bounds.height)
            }
            ObstacleShape.CIRCLE -> {
                val centerX = bounds.x + bounds.width / 2
                val centerY = bounds.y + bounds.height / 2
                val radius = bounds.width / 2

                // Main circle
                shapeRenderer.setColor(Constants.OBSTACLE_COLOR_1)
                shapeRenderer.circle(centerX, centerY, radius)

                // Inner darker circle for depth
                shapeRenderer.setColor(Constants.OBSTACLE_COLOR_2)
                shapeRenderer.circle(centerX, centerY - radius * 0.2f, radius * 0.7f)

                // Small highlight
                shapeRenderer.setColor(1f, 0.5f, 0.5f, 0.6f)
                shapeRenderer.circle(centerX - radius * 0.3f, centerY + radius * 0.3f, radius * 0.2f)
            }
            ObstacleShape.TRIANGLE -> {
                val x1 = bounds.x + bounds.width / 2
                val y1 = bounds.y + bounds.height
                val x2 = bounds.x
                val y2 = bounds.y
                val x3 = bounds.x + bounds.width
                val y3 = bounds.y

                // Main triangle
                shapeRenderer.setColor(Constants.OBSTACLE_COLOR_1)
                shapeRenderer.triangle(x1, y1, x2, y2, x3, y3)

                // Darker bottom half
                shapeRenderer.setColor(Constants.OBSTACLE_COLOR_2)
                shapeRenderer.triangle(x1, y1 - bounds.height / 2, x2, y2, x3, y3)
            }
        }
    }

    fun getBounds(): Rectangle = bounds

    fun getX(): Float = bounds.x
}