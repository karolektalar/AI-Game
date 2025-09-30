package com.game.jumper.entities

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.game.jumper.utils.Constants

/**
 * Player entity - represents the jumping character
 * Handles jumping physics and collision detection
 */
class Player(x: Float, y: Float) {

    private val bounds = Rectangle(x, y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT)
    private var velocityY = 0f
    private var isOnGround = false

    fun update(delta: Float) {
        // Apply gravity
        velocityY += Constants.GRAVITY * delta

        // Update position
        bounds.y += velocityY * delta

        // Check ground collision
        if (bounds.y <= Constants.GROUND_HEIGHT) {
            bounds.y = Constants.GROUND_HEIGHT
            velocityY = 0f
            isOnGround = true
        } else {
            isOnGround = false
        }
    }

    /**
     * Make the player jump if on ground
     */
    fun jump() {
        if (isOnGround) {
            velocityY = Constants.JUMP_VELOCITY
            isOnGround = false
        }
    }

    /**
     * Check collision with an obstacle
     */
    fun collidesWith(obstacle: Obstacle): Boolean {
        return bounds.overlaps(obstacle.getBounds())
    }

    /**
     * Render the player as a colored rectangle
     */
    fun render(shapeRenderer: ShapeRenderer) {
        shapeRenderer.setColor(Constants.PLAYER_COLOR)
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height)
    }

    fun getBounds(): Rectangle = bounds

    fun getX(): Float = bounds.x

    fun getY(): Float = bounds.y

    fun reset() {
        bounds.x = Constants.PLAYER_START_X
        bounds.y = Constants.PLAYER_START_Y
        velocityY = 0f
        isOnGround = false
    }
}