package com.example

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.alarm.AlarmReceiver
import com.example.ui.WakeGrooveMainApp
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.WakeGrooveViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: WakeGrooveViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        configureLockScreenWakeUp()

        handleAlarmIntent(intent)

        setContent {
            MyApplicationTheme {
                WakeGrooveMainApp(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleAlarmIntent(intent)
    }

    private fun handleAlarmIntent(intent: Intent?) {
        if (intent == null) return
        val isRinging = intent.getBooleanExtra(AlarmReceiver.EXTRA_IS_RINGING, false)
        if (isRinging) {
            val label = intent.getStringExtra(AlarmReceiver.EXTRA_ALARM_LABEL) ?: "Morning Wake-Up Groove"
            val danceSeconds = intent.getIntExtra(AlarmReceiver.EXTRA_DANCE_SECONDS, 15)
            viewModel.handleIncomingAlarm(label, danceSeconds)
        }
    }

    private fun configureLockScreenWakeUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
            keyguardManager?.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }
    }
}
