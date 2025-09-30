package com.game.jumper.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.game.jumper.utils.Constants

/**
 * Bullet entity - projectile that destroys obstacles
 */
class Bullet(
    x: Float,
    y: Float,
    private val weaponType: WeaponType = WeaponType.NORMAL,
    private val angleOffset: Float = 0f // For spread shots
) {

    private val bounds: Rectangle
    private val speed: Float
    private val color: Color
    private var velocityY: Float = 0f

    init {
        // Set bullet properties based on weapon type
        when (weaponType) {
            WeaponType.NORMAL -> {
                bounds = Rectangle(x, y, Constants.BULLET_WIDTH, Constants.BULLET_HEIGHT)
                speed = Constants.BULLET_SPEED
                color = Constants.BULLET_COLOR
            }
            WeaponType.RAPID_FIRE -> {
                bounds = Rectangle(x, y, Constants.RAPID_FIRE_BULLET_WIDTH, Constants.RAPID_FIRE_BULLET_HEIGHT)
                speed = Constants.RAPID_FIRE_SPEED
                color = Constants.RAPID_FIRE_COLOR
            }
            WeaponType.LASER -> {
                bounds = Rectangle(x, y, Constants.LASER_BULLET_WIDTH, Constants.LASER_BULLET_HEIGHT)
                speed = Constants.LASER_SPEED
                color = Constants.LASER_COLOR
            }
            WeaponType.SPREAD -> {
                bounds = Rectangle(x, y, Constants.SPREAD_BULLET_WIDTH, Constants.SPREAD_BULLET_HEIGHT)
                speed = Constants.SPREAD_SPEED
                color = Constants.SPREAD_COLOR
                // Calculate vertical velocity based on angle offset
                velocityY = speed * angleOffset
            }
        }
    }

    fun update(delta: Float) {
        // Move bullet horizontally
        bounds.x += speed * delta
        // Move bullet vertically (for spread shots)
        bounds.y += velocityY * delta
    }

    /**
     * Check if bullet is off screen (right side)
     */
    fun isOffScreen(screenWidth: Float): Boolean {
        return bounds.x > screenWidth
    }

    /**
     * Check collision with an obstacle
     */
    fun collidesWith(obstacle: Obstacle): Boolean {
        return bounds.overlaps(obstacle.getBounds())
    }

    /**
     * Render the bullet as a colored rectangle
     */
    fun render(shapeRenderer: ShapeRenderer) {
        shapeRenderer.setColor(color)
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height)
    }

    fun getBounds(): Rectangle = bounds
}