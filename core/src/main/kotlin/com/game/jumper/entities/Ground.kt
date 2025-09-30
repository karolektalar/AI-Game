package com.game.jumper.entities

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.game.jumper.JumperGame
import com.game.jumper.utils.Constants

/**
 * Ground entity - represents the ground that player stands on
 */
class Ground {

    fun render(shapeRenderer: ShapeRenderer) {
        // Gradient ground
        val steps = 10
        val stepHeight = Constants.GROUND_HEIGHT / steps
        for (i in 0 until steps) {
            val ratio = i.toFloat() / steps
            val r = Constants.GROUND_COLOR_1.r + (Constants.GROUND_COLOR_2.r - Constants.GROUND_COLOR_1.r) * ratio
            val g = Constants.GROUND_COLOR_1.g + (Constants.GROUND_COLOR_2.g - Constants.GROUND_COLOR_1.g) * ratio
            val b = Constants.GROUND_COLOR_1.b + (Constants.GROUND_COLOR_2.b - Constants.GROUND_COLOR_1.b) * ratio
            shapeRenderer.setColor(r, g, b, 1f)
            shapeRenderer.rect(0f, (steps - i - 1) * stepHeight, JumperGame.GAME_WIDTH, stepHeight)
        }

        // Add danger line at top of ground
        shapeRenderer.setColor(1f, 0f, 0f, 0.8f)
        shapeRenderer.rect(0f, Constants.GROUND_HEIGHT - 3, JumperGame.GAME_WIDTH, 3f)
    }
}