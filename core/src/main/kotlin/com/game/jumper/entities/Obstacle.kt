package com.game.jumper.entities

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Rectangle
import com.game.jumper.utils.Constants
import kotlin.math.cos
import kotlin.math.sin

/**
 * Obstacle entity - represents moving obstacles that player must avoid
 */
class Obstacle(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    val shape: ObstacleShape = ObstacleShape.RECTANGLE,
    private val speed: Float = Constants.OBSTACLE_SPEED
) {

    private val bounds = Rectangle(x, y, width, height)
    private val circle = Circle(x + width / 2, y + height / 2, width / 2) // For circular collision
    private var scored = false // Track if this obstacle has been scored

    fun update(delta: Float) {
        // Move obstacle from right to left
        bounds.x -= speed * delta
        // Update circle position for circular obstacles
        circle.x = bounds.x + bounds.width / 2
        circle.y = bounds.y + bounds.height / 2
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
                // Main circle
                shapeRenderer.setColor(Constants.OBSTACLE_COLOR_1)
                shapeRenderer.circle(circle.x, circle.y, circle.radius)

                // Inner darker circle for depth
                shapeRenderer.setColor(Constants.OBSTACLE_COLOR_2)
                shapeRenderer.circle(circle.x, circle.y - circle.radius * 0.2f, circle.radius * 0.7f)

                // Small highlight
                shapeRenderer.setColor(1f, 0.5f, 0.5f, 0.6f)
                shapeRenderer.circle(circle.x - circle.radius * 0.3f, circle.y + circle.radius * 0.3f, circle.radius * 0.2f)
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