package com.game.jumper.effects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import kotlin.random.Random

/**
 * Particle for visual effects
 */
class Particle(
    var x: Float,
    var y: Float,
    private val velocityX: Float,
    private val velocityY: Float,
    private val color: Color,
    private val size: Float,
    private val lifetime: Float
) {
    private var age = 0f
    var alpha = 1f

    fun update(delta: Float): Boolean {
        age += delta
        x += velocityX * delta
        y += velocityY * delta

        // Fade out over lifetime
        alpha = 1f - (age / lifetime)

        return age < lifetime
    }

    fun render(shapeRenderer: ShapeRenderer) {
        val particleColor = Color(color.r, color.g, color.b, alpha)
        shapeRenderer.setColor(particleColor)
        shapeRenderer.circle(x, y, size)
    }
}

/**
 * Particle system for managing multiple particles
 */
class ParticleSystem {
    private val particles = mutableListOf<Particle>()

    fun addExplosion(x: Float, y: Float, color: Color, count: Int = 20) {
        repeat(count) {
            val angle = Random.nextFloat() * Math.PI.toFloat() * 2
            val speed = 50f + Random.nextFloat() * 100f
            val velocityX = kotlin.math.cos(angle) * speed
            val velocityY = kotlin.math.sin(angle) * speed
            val size = 2f + Random.nextFloat() * 4f
            val lifetime = 0.3f + Random.nextFloat() * 0.5f

            particles.add(Particle(x, y, velocityX, velocityY, color, size, lifetime))
        }
    }

    fun addShootEffect(x: Float, y: Float, color: Color) {
        repeat(5) {
            val velocityX = -50f + Random.nextFloat() * 100f
            val velocityY = -50f + Random.nextFloat() * 100f
            val size = 1f + Random.nextFloat() * 2f
            val lifetime = 0.2f + Random.nextFloat() * 0.3f

            particles.add(Particle(x, y, velocityX, velocityY, color, size, lifetime))
        }
    }

    fun update(delta: Float) {
        val iterator = particles.iterator()
        while (iterator.hasNext()) {
            val particle = iterator.next()
            if (!particle.update(delta)) {
                iterator.remove()
            }
        }
    }

    fun render(shapeRenderer: ShapeRenderer) {
        particles.forEach { it.render(shapeRenderer) }
    }

    fun clear() {
        particles.clear()
    }
}