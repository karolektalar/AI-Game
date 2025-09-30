package com.game.jumper.entities

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.game.jumper.utils.Constants

/**
 * Bullet shot by enemies towards the left
 */
class EnemyBullet(x: Float, y: Float) {
    private val bounds = Rectangle(x, y, Constants.ENEMY_BULLET_WIDTH, Constants.ENEMY_BULLET_HEIGHT)

    fun update(delta: Float) {
        // Move left
        bounds.x -= Constants.ENEMY_BULLET_SPEED * delta
    }

    fun render(renderer: ShapeRenderer) {
        renderer.color = Constants.ENEMY_BULLET_COLOR

        // Draw as a glowing circle
        val centerX = bounds.x + bounds.width / 2
        val centerY = bounds.y + bounds.height / 2
        val radius = bounds.width / 2

        renderer.circle(centerX, centerY, radius, 16)

        // Inner glow
        renderer.color = Constants.ENEMY_BULLET_COLOR.cpy().lerp(com.badlogic.gdx.graphics.Color.WHITE, 0.5f)
        renderer.circle(centerX, centerY, radius * 0.5f, 12)
    }

    fun collidesWith(player: Player): Boolean {
        return bounds.overlaps(player.getBounds())
    }

    fun isOffScreen(screenWidth: Float): Boolean {
        return bounds.x + bounds.width < 0
    }

    fun getBounds(): Rectangle = bounds
}
