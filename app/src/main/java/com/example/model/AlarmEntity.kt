package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hour: Int,
    val minute: Int,
    val label: String = "Morning Dance Groove",
    val isEnabled: Boolean = true,
    val repeatDays: String = "1,2,3,4,5", // 1=Sun, 2=Mon, 3=Tue, 4=Wed, 5=Thu, 6=Fri, 7=Sat
    val danceTargetSeconds: Int = 20,
    val danceIntensity: String = "Medium Groove", // "Gentle Groove", "Medium Groove", "Wild Disco"
    val requireSelfie: Boolean = true,
    val alarmRingtone: String = "Disco Funk Beat",
    val vibrate: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun formattedTime(): String {
        val h = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        val amPm = if (hour < 12) "AM" else "PM"
        val m = String.format("%02d", minute)
        return "$h:$m $amPm"
    }

    fun repeatDaysSummary(): String {
        if (repeatDays.isBlank()) return "Once"
        val days = repeatDays.split(",").mapNotNull { it.trim().toIntOrNull() }
        if (days.size == 7) return "Every day"
        if (days.toSet() == setOf(2, 3, 4, 5, 6)) return "Weekdays"
        if (days.toSet() == setOf(1, 7)) return "Weekends"
        val dayNames = mapOf(1 to "Sun", 2 to "Mon", 3 to "Tue", 4 to "Wed", 5 to "Thu", 6 to "Fri", 7 to "Sat")
        return days.mapNotNull { dayNames[it] }.joinToString(", ")
    }
}
