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
    private var jumpCount = 0
    private var currentWeapon = WeaponType.NORMAL
    private var hasPowerJump = false
    private var skin = PlayerSkin.DEFAULT

    fun update(delta: Float) {
        // Apply gravity
        velocityY += Constants.GRAVITY * delta

        // Update position
        bounds.y += velocityY * delta

        // Check ground collision - hitting ground is death
        if (bounds.y <= Constants.GROUND_HEIGHT) {
            // Player hit the ground - game over
            bounds.y = Constants.GROUND_HEIGHT
        } else {
            isOnGround = false
        }
    }

    /**
     * Check if player fell to the ground
     */
    fun isOnGround(): Boolean {
        return bounds.y <= Constants.GROUND_HEIGHT
    }

    /**
     * Reset jump count when appropriate (not used for ground anymore)
     */
    fun resetJumpCount() {
        jumpCount = 0
    }

    /**
     * Make the player jump (supports multi-jump)
     */
    fun jump() {
        // Reset jump count when falling (allows continuous jumping)
        if (velocityY < 0) {
            jumpCount = 0
        }

        // Can always jump in the air now (no ground requirement)
        if (jumpCount < Constants.MAX_JUMPS) {
            velocityY = if (hasPowerJump) Constants.POWER_JUMP_VELOCITY else Constants.JUMP_VELOCITY
            jumpCount++
        }
    }

    /**
     * Check collision with an obstacle
     */
    fun collidesWith(obstacle: Obstacle): Boolean {
        return bounds.overlaps(obstacle.getBounds())
    }

    /**
     * Render the player with enhanced visuals using current skin
     */
    fun render(shapeRenderer: ShapeRenderer) {
        // Main body
        shapeRenderer.setColor(skin.primaryColor)
        shapeRenderer.rect(bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4)

        // Outline/border effect
        shapeRenderer.setColor(skin.outlineColor)
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, 2f) // Bottom
        shapeRenderer.rect(bounds.x, bounds.y + bounds.height - 2, bounds.width, 2f) // Top
        shapeRenderer.rect(bounds.x, bounds.y, 2f, bounds.height) // Left
        shapeRenderer.rect(bounds.x + bounds.width - 2, bounds.y, 2f, bounds.height) // Right

        // Highlight effect
        shapeRenderer.setColor(skin.highlightColor)
        shapeRenderer.rect(bounds.x + 5, bounds.y + bounds.height - 8, bounds.width - 10, 3f)
    }

    /**
     * Set player skin
     */
    fun setSkin(newSkin: PlayerSkin) {
        skin = newSkin
    }

    fun getSkin(): PlayerSkin = skin

    fun getBounds(): Rectangle = bounds

    fun getX(): Float = bounds.x

    fun getY(): Float = bounds.y

    /**
     * Shoot bullet(s) from the player's position based on current weapon
     */
    fun shoot(): List<Bullet> {
        val bullets = mutableListOf<Bullet>()
        val bulletX = bounds.x + bounds.width
        val bulletY = bounds.y + bounds.height / 2

        when (currentWeapon) {
            WeaponType.NORMAL -> {
                bullets.add(Bullet(bulletX, bulletY, WeaponType.NORMAL))
            }
            WeaponType.RAPID_FIRE -> {
                bullets.add(Bullet(bulletX, bulletY, WeaponType.RAPID_FIRE))
            }
            WeaponType.LASER -> {
                bullets.add(Bullet(bulletX, bulletY, WeaponType.LASER))
            }
            WeaponType.SPREAD -> {
                // Shoot 3 bullets in a spread pattern
                bullets.add(Bullet(bulletX, bulletY, WeaponType.SPREAD, 0f))       // Center
                bullets.add(Bullet(bulletX, bulletY, WeaponType.SPREAD, 0.3f))     // Up
                bullets.add(Bullet(bulletX, bulletY, WeaponType.SPREAD, -0.3f))    // Down
            }
        }

        return bullets
    }

    /**
     * Set the current weapon
     */
    fun setWeapon(weaponType: WeaponType) {
        currentWeapon = weaponType
    }

    /**
     * Enable/disable power jump
     */
    fun setPowerJump(enabled: Boolean) {
        hasPowerJump = enabled
    }

    fun getCurrentWeapon(): WeaponType = currentWeapon

    fun hasPowerJump(): Boolean = hasPowerJump

    fun reset() {
        bounds.x = Constants.PLAYER_START_X
        bounds.y = Constants.PLAYER_START_Y
        velocityY = 0f
        isOnGround = false
        jumpCount = 0
        currentWeapon = WeaponType.NORMAL
        hasPowerJump = false
    }
}