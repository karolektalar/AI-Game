package com.game.jumper.utils

import com.badlogic.gdx.graphics.Color

/**
 * Game constants for easy tweaking and balancing
 */
object Constants {
    // Physics
    const val GRAVITY = -980f
    const val JUMP_VELOCITY = 450f
    const val PLAYER_SPEED = 0f // Player doesn't move horizontally

    // Player
    const val PLAYER_WIDTH = 50f
    const val PLAYER_HEIGHT = 50f
    const val PLAYER_START_X = 100f
    const val PLAYER_START_Y = 200f

    // Obstacles
    const val OBSTACLE_WIDTH = 50f
    const val OBSTACLE_HEIGHT = 50f
    const val OBSTACLE_SPEED = 200f
    const val OBSTACLE_SPAWN_INTERVAL = 2f // seconds
    const val OBSTACLE_MIN_GAP = 150f
    const val OBSTACLE_MAX_GAP = 300f

    // Ground
    const val GROUND_HEIGHT = 100f

    // Colors
    val PLAYER_COLOR = Color.GREEN
    val OBSTACLE_COLOR = Color.RED
    val GROUND_COLOR = Color.BROWN
    val BACKGROUND_COLOR = Color(0.5f, 0.7f, 1f, 1f) // Sky blue

    // Score
    const val SCORE_PER_OBSTACLE = 1

    // Ads
    const val SHOW_INTERSTITIAL_EVERY = 3 // Show interstitial every N game overs
}