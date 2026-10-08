package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "wake_up_records")
data class WakeUpRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val alarmLabel: String = "Morning Alarm",
    val danceSecondsCompleted: Int = 20,
    val danceMovesCount: Int = 42,
    val grooveRating: String = "Dance Machine 💃🔥",
    val selfieImagePath: String? = null,
    val isVerified: Boolean = true
) {
    fun formattedDate(): String {
        val sdf = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun formattedTime(): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
