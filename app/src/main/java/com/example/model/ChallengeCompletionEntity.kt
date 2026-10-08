package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "challenge_completions")
data class ChallengeCompletionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val challengeId: String,
    val challengeName: String,
    val challengeEmoji: String,
    val completedAt: Long = System.currentTimeMillis(),
    val durationSeconds: Int,
    val selfieImagePath: String? = null,
    val movesDetected: Int = 35,
    val grooveScore: Int = 100 // 0 to 100
) {
    fun formattedDate(): String {
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return sdf.format(Date(completedAt))
    }

    fun formattedTime(): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(Date(completedAt))
    }
}
