package com.example.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

data class DanceMotionState(
    val isDancing: Boolean = false,
    val currentIntensity: Float = 0f, // 0.0 to 1.0
    val totalMovesCount: Int = 0,
    val accumulatedDanceSeconds: Float = 0f,
    val targetSeconds: Int = 15,
    val progress: Float = 0f, // 0.0 to 1.0
    val comboMultiplier: Int = 1,
    val statusText: String = "Start Moving!"
)

class DanceMotionDetector(
    context: Context,
    private val targetSeconds: Int = 15,
    private val onStateChanged: (DanceMotionState) -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastUpdateTimestamp = 0L
    private var lastMoveTimestamp = 0L
    private var movesCount = 0
    private var danceSeconds = 0f
    private var combo = 1
    private var consecutiveMoves = 0

    // Acceleration tracking
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private var isInitialized = false

    fun start() {
        reset()
        accelerometer?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    fun reset() {
        movesCount = 0
        danceSeconds = 0f
        combo = 1
        consecutiveMoves = 0
        lastUpdateTimestamp = System.currentTimeMillis()
        lastMoveTimestamp = 0L
        isInitialized = false
        emitState(0f, false)
    }

    // Allows manual stimulation (e.g. In emulator or button tap)
    fun simulateDanceMove(boostFactor: Float = 1.0f) {
        val now = System.currentTimeMillis()
        recordDanceMovement(intensity = 0.85f * boostFactor, now = now)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val now = System.currentTimeMillis()
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        if (!isInitialized) {
            lastX = x
            lastY = y
            lastZ = z
            isInitialized = true
            lastUpdateTimestamp = now
            return
        }

        // Calculate delta acceleration (jerk)
        val deltaX = abs(lastX - x)
        val deltaY = abs(lastY - y)
        val deltaZ = abs(lastZ - z)
        val speed = sqrt((deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ).toDouble()).toFloat()

        lastX = x
        lastY = y
        lastZ = z

        // Sensitivity threshold for dance movement
        val danceThreshold = 3.2f

        if (speed > danceThreshold) {
            val intensity = min(1.0f, (speed - danceThreshold) / 15.0f + 0.3f)
            recordDanceMovement(intensity, now)
        } else {
            // Decay dance state if user stopped moving
            if (now - lastMoveTimestamp > 1200L) {
                combo = 1
                consecutiveMoves = 0
                emitState(0f, false)
            }
        }
    }

    private fun recordDanceMovement(intensity: Float, now: Long) {
        val timeSinceLast = if (lastUpdateTimestamp > 0) (now - lastUpdateTimestamp) / 1000f else 0.05f
        lastUpdateTimestamp = now

        // Limit delta time addition to avoid huge jumps
        val clampedDelta = min(0.3f, max(0.02f, timeSinceLast))
        danceSeconds += clampedDelta

        if (now - lastMoveTimestamp > 250L) { // Min 250ms per distinct move
            movesCount++
            consecutiveMoves++
            if (consecutiveMoves % 5 == 0 && combo < 5) {
                combo++
            }
            lastMoveTimestamp = now
        }

        emitState(intensity, isDancing = true)
    }

    private fun emitState(intensity: Float, isDancing: Boolean) {
        val progress = min(1.0f, danceSeconds / targetSeconds.toFloat())
        val status = when {
            progress >= 1.0f -> "MISSION COMPLETE! 🎉"
            combo >= 4 -> "SUPER GROOVER! ⚡🔥"
            combo >= 2 -> "FEEL THE RHYTHM! 🕺"
            isDancing -> "KEEP DANCING! 💃"
            else -> "Shake & Groove!"
        }

        onStateChanged(
            DanceMotionState(
                isDancing = isDancing,
                currentIntensity = intensity,
                totalMovesCount = movesCount,
                accumulatedDanceSeconds = danceSeconds,
                targetSeconds = targetSeconds,
                progress = progress,
                comboMultiplier = combo,
                statusText = status
            )
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
