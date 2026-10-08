package com.example.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.example.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_TRIGGER_ALARM = "com.aistudio.wakegroove.ACTION_TRIGGER_ALARM"
        const val EXTRA_ALARM_ID = "extra_alarm_id"
        const val EXTRA_ALARM_LABEL = "extra_alarm_label"
        const val EXTRA_DANCE_SECONDS = "extra_dance_seconds"
        const val EXTRA_REQUIRE_SELFIE = "extra_require_selfie"
        const val EXTRA_DANCE_INTENSITY = "extra_dance_intensity"
        const val EXTRA_IS_RINGING = "extra_is_ringing"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("AlarmReceiver", "Alarm broadcast received!")

        val alarmId = intent?.getLongExtra(EXTRA_ALARM_ID, -1L) ?: -1L
        val label = intent?.getStringExtra(EXTRA_ALARM_LABEL) ?: "Morning Wake-Up Groove"
        val danceSeconds = intent?.getIntExtra(EXTRA_DANCE_SECONDS, 20) ?: 20
        val requireSelfie = intent?.getBooleanExtra(EXTRA_REQUIRE_SELFIE, true) ?: true

        // Acquire brief wake lock to launch service and activity
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        val wakeLock = powerManager?.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "wakegroove:receiver_wakelock"
        )
        wakeLock?.acquire(3000L)

        // Start loud audio and vibration service
        AlarmAudioService.start(context, label)

        // Launch MainActivity directly
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_IS_RINGING, true)
            putExtra(EXTRA_ALARM_ID, alarmId)
            putExtra(EXTRA_ALARM_LABEL, label)
            putExtra(EXTRA_DANCE_SECONDS, danceSeconds)
            putExtra(EXTRA_REQUIRE_SELFIE, requireSelfie)
        }
        context.startActivity(activityIntent)
    }
}
