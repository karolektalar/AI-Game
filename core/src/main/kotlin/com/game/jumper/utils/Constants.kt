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
    const val PLAYER_START_Y = 400f // Middle of screen (800/2)

    // Obstacles
    const val OBSTACLE_MIN_WIDTH = 30f
    const val OBSTACLE_MAX_WIDTH = 80f
    const val OBSTACLE_MIN_HEIGHT = 30f
    const val OBSTACLE_MAX_HEIGHT = 80f
    const val OBSTACLE_SPEED = 200f
    const val OBSTACLE_SPAWN_INTERVAL = 0.8f // seconds - much more frequent
    const val OBSTACLE_MIN_GAP = 150f
    const val OBSTACLE_MAX_GAP = 300f

    // Ground
    const val GROUND_HEIGHT = 100f

    // Colors - Enhanced palette
    val PLAYER_COLOR = Color(0.2f, 0.9f, 0.4f, 1f) // Vibrant green
    val PLAYER_OUTLINE = Color(0.1f, 0.5f, 0.2f, 1f) // Dark green

    val OBSTACLE_COLOR_1 = Color(0.9f, 0.2f, 0.3f, 1f) // Vibrant red
    val OBSTACLE_COLOR_2 = Color(0.7f, 0.1f, 0.2f, 1f) // Dark red
    val OBSTACLE_OUTLINE = Color(0.4f, 0.05f, 0.1f, 1f)

    val GROUND_COLOR_1 = Color(0.3f, 0.25f, 0.2f, 1f) // Dark brown
    val GROUND_COLOR_2 = Color(0.2f, 0.15f, 0.1f, 1f) // Darker brown

    val BACKGROUND_TOP = Color(0.1f, 0.1f, 0.2f, 1f) // Dark blue/purple
    val BACKGROUND_BOTTOM = Color(0.4f, 0.2f, 0.5f, 1f) // Purple

    val UI_TEXT_COLOR = Color(1f, 1f, 1f, 1f) // White
    val UI_SHADOW_COLOR = Color(0f, 0f, 0f, 0.5f) // Semi-transparent black

    // Player abilities
    const val MAX_JUMPS = 3 // Multi-jump capability
    const val POWER_JUMP_VELOCITY = 650f // Stronger jump

    // Shooting
    const val BULLET_WIDTH = 12f
    const val BULLET_HEIGHT = 6f
    const val BULLET_SPEED = 400f
    const val SHOOT_COOLDOWN = 0.3f // Seconds between shots
    val BULLET_COLOR = Color(1f, 0.9f, 0.2f, 1f) // Bright yellow
    val BULLET_GLOW = Color(1f, 0.7f, 0f, 0.6f) // Orange glow

    // Weapons
    const val RAPID_FIRE_BULLET_WIDTH = 8f
    const val RAPID_FIRE_BULLET_HEIGHT = 4f
    const val RAPID_FIRE_SPEED = 500f
    val RAPID_FIRE_COLOR = Color.ORANGE

    const val LASER_BULLET_WIDTH = 20f
    const val LASER_BULLET_HEIGHT = 3f
    const val LASER_SPEED = 700f
    val LASER_COLOR = Color.CYAN

    const val SPREAD_BULLET_WIDTH = 8f
    const val SPREAD_BULLET_HEIGHT = 8f
    const val SPREAD_SPEED = 350f
    val SPREAD_COLOR = Color.MAGENTA

    // PowerUps
    const val POWERUP_SIZE = 30f
    const val POWERUP_FALL_SPEED = 80f
    const val POWERUP_DROP_CHANCE = 0.5f // 50% chance (increased for testing)
    val POWERUP_WEAPON_COLOR = Color.GOLD
    val POWERUP_JUMP_COLOR = Color.SKY

    // Score
    const val SCORE_PER_OBSTACLE = 1

    // Ads
    const val SHOW_INTERSTITIAL_EVERY = 3 // Show interstitial every N game overs
}