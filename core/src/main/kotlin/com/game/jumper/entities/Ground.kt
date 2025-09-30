package com.game.jumper.entities

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.game.jumper.JumperGame
import com.game.jumper.utils.Constants

/**
 * Ground entity - represents the ground that player stands on
 */
class Ground {

    fun render(shapeRenderer: ShapeRenderer) {
        shapeRenderer.setColor(Constants.GROUND_COLOR)
        shapeRenderer.rect(
            0f,
            0f,
            JumperGame.GAME_WIDTH,
            Constants.GROUND_HEIGHT
        )
    }
}