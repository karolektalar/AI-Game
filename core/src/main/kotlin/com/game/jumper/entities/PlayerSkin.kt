package com.game.jumper.entities

import com.badlogic.gdx.graphics.Color

/**
 * Different player skins
 */
enum class PlayerSkin(
    val displayName: String,
    val primaryColor: Color,
    val outlineColor: Color,
    val highlightColor: Color
) {
    DEFAULT(
        "Classic",
        Color(0.2f, 0.9f, 0.4f, 1f), // Vibrant green
        Color(0.1f, 0.5f, 0.2f, 1f), // Dark green
        Color(0.4f, 1f, 0.6f, 0.8f)  // Light green
    ),
    FIRE(
        "Fire",
        Color(1f, 0.3f, 0.1f, 1f),   // Orange-red
        Color(0.6f, 0.1f, 0f, 1f),   // Dark red
        Color(1f, 0.8f, 0.2f, 0.9f)  // Yellow
    ),
    ICE(
        "Ice",
        Color(0.4f, 0.8f, 1f, 1f),   // Light blue
        Color(0.1f, 0.3f, 0.6f, 1f), // Dark blue
        Color(0.8f, 1f, 1f, 0.9f)    // White-blue
    ),
    ELECTRIC(
        "Electric",
        Color(1f, 1f, 0.2f, 1f),     // Bright yellow
        Color(0.6f, 0.5f, 0f, 1f),   // Dark yellow
        Color(1f, 1f, 1f, 0.9f)      // White
    ),
    SHADOW(
        "Shadow",
        Color(0.3f, 0.2f, 0.4f, 1f), // Dark purple
        Color(0.1f, 0.05f, 0.15f, 1f), // Very dark
        Color(0.6f, 0.4f, 0.8f, 0.8f)  // Light purple
    ),
    RAINBOW(
        "Rainbow",
        Color(1f, 0.5f, 0.8f, 1f),   // Pink
        Color(0.5f, 0.2f, 0.6f, 1f), // Purple
        Color(0.5f, 0.9f, 1f, 0.9f)  // Cyan
    )
}