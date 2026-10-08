package com.example.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.MainActivity
import com.example.model.AlarmEntity
import java.util.Calendar

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun schedule(alarm: AlarmEntity) {
        if (!alarm.isEnabled) {
            cancel(alarm)
            return
        }

        val triggerTimeMs = calculateNextTriggerTime(alarm.hour, alarm.minute, alarm.repeatDays)
        Log.d("AlarmScheduler", "Scheduling alarm ${alarm.id} for $triggerTimeMs (hour=${alarm.hour}, min=${alarm.minute})")

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_TRIGGER_ALARM
            putExtra(AlarmReceiver.EXTRA_ALARM_ID, alarm.id)
            putExtra(AlarmReceiver.EXTRA_ALARM_LABEL, alarm.label)
            putExtra(AlarmReceiver.EXTRA_DANCE_SECONDS, alarm.danceTargetSeconds)
            putExtra(AlarmReceiver.EXTRA_REQUIRE_SELFIE, alarm.requireSelfie)
            putExtra(AlarmReceiver.EXTRA_DANCE_INTENSITY, alarm.danceIntensity)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val showIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(AlarmReceiver.EXTRA_ALARM_ID, alarm.id)
        }
        val showPendingIntent = PendingIntent.getActivity(
            context,
            alarm.id.toInt(),
            showIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager?.let { am ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerTimeMs, showPendingIntent)
                am.setAlarmClock(alarmClockInfo, pendingIntent)
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMs, pendingIntent)
            }
        }
    }

    fun cancel(alarm: AlarmEntity) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_TRIGGER_ALARM
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager?.cancel(pendingIntent)
    }

    fun scheduleQuickTest(secondsFromNow: Int = 5, label: String = "Test Groove") {
        val triggerTimeMs = System.currentTimeMillis() + (secondsFromNow * 1000L)
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_TRIGGER_ALARM
            putExtra(AlarmReceiver.EXTRA_ALARM_ID, -1L)
            putExtra(AlarmReceiver.EXTRA_ALARM_LABEL, label)
            putExtra(AlarmReceiver.EXTRA_DANCE_SECONDS, 15)
            putExtra(AlarmReceiver.EXTRA_REQUIRE_SELFIE, true)
            putExtra(AlarmReceiver.EXTRA_DANCE_INTENSITY, "Medium Groove")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            99999,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val showIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val showPendingIntent = PendingIntent.getActivity(
            context,
            99999,
            showIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager?.let { am ->
            val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerTimeMs, showPendingIntent)
            am.setAlarmClock(alarmClockInfo, pendingIntent)
        }
    }

    private fun calculateNextTriggerTime(hour: Int, minute: Int, repeatDaysString: String): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val now = System.currentTimeMillis()
        val repeatDays = if (repeatDaysString.isNotBlank()) {
            repeatDaysString.split(",").mapNotNull { it.trim().toIntOrNull() }
        } else emptyList()

        if (repeatDays.isEmpty()) {
            // One-time alarm
            if (calendar.timeInMillis <= now) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
            return calendar.timeInMillis
        }

        // Repeating on specific days (1=Sunday ... 7=Saturday in Calendar)
        for (i in 0..7) {
            val checkCal = (calendar.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, i)
            }
            val dayOfWeek = checkCal.get(Calendar.DAY_OF_WEEK)
            if (repeatDays.contains(dayOfWeek) && checkCal.timeInMillis > now) {
                return checkCal.timeInMillis
            }
        }

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar.timeInMillis
    }
}
