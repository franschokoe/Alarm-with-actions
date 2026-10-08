package com.example.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AlarmEntity
import com.example.model.DanceChallenge
import com.example.ui.screens.AlarmsScreen
import com.example.ui.screens.ChallengesScreen
import com.example.ui.screens.JournalScreen
import com.example.ui.screens.WakeMissionScreen
import com.example.ui.theme.DeepMidnight
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SunriseGold
import com.example.viewmodel.WakeGrooveViewModel
import com.example.viewmodel.WakeMissionStage

enum class AppTab {
    ALARMS,
    CHALLENGES,
    JOURNAL
}

@Composable
fun WakeGrooveMainApp(
    viewModel: WakeGrooveViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentTab by remember { mutableStateOf(AppTab.ALARMS) }

    val alarms by viewModel.allAlarms.collectAsStateWithLifecycle()
    val records by viewModel.allRecords.collectAsStateWithLifecycle()
    val completions by viewModel.allCompletions.collectAsStateWithLifecycle()
    val todayChallenge by viewModel.todayChallenge.collectAsStateWithLifecycle()
    val activeChallenge by viewModel.activeChallenge.collectAsStateWithLifecycle()

    val isAlarmRinging by viewModel.isAlarmRinging.collectAsStateWithLifecycle()
    val missionStage by viewModel.missionStage.collectAsStateWithLifecycle()
    val danceMotionState by viewModel.danceMotionState.collectAsStateWithLifecycle()
    val activeAlarmLabel by viewModel.activeAlarmLabel.collectAsStateWithLifecycle()

    // Request permissions on first launch
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ -> }

    LaunchedEffect(Unit) {
        val permissionsToRequest = mutableListOf(Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        val notGranted = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        if (notGranted.isNotEmpty()) {
            permissionLauncher.launch(notGranted.toTypedArray())
        }
    }

    // If alarm is ringing or mission stage is active (dance, selfie, celebration)
    if (missionStage != WakeMissionStage.READY || isAlarmRinging) {
        WakeMissionScreen(
            stage = missionStage,
            challenge = activeChallenge,
            alarmLabel = activeAlarmLabel,
            motionState = danceMotionState,
            isRinging = isAlarmRinging,
            onSimulateDanceStep = { viewModel.simulateDanceStep() },
            onSelfieConfirmed = { path -> viewModel.onSelfieConfirmed(path) },
            onDismissMission = { viewModel.dismissMission() },
            onSnooze = { viewModel.snoozeAlarm() }
        )
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color.White,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    NavigationBarItem(
                        selected = currentTab == AppTab.ALARMS,
                        onClick = { currentTab = AppTab.ALARMS },
                        icon = {
                            Icon(
                                if (currentTab == AppTab.ALARMS) Icons.Filled.Alarm else Icons.Outlined.Alarm,
                                contentDescription = "Alarms"
                            )
                        },
                        label = { Text("Alarms", fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = SunriseGold,
                            indicatorColor = ElectricPurple,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("tab_alarms")
                    )

                    NavigationBarItem(
                        selected = currentTab == AppTab.CHALLENGES,
                        onClick = { currentTab = AppTab.CHALLENGES },
                        icon = {
                            Icon(
                                if (currentTab == AppTab.CHALLENGES) Icons.Filled.EmojiEvents else Icons.Outlined.EmojiEvents,
                                contentDescription = "Challenges"
                            )
                        },
                        label = { Text("Challenges", fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = SunriseGold,
                            indicatorColor = NeonPink,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("tab_challenges")
                    )

                    NavigationBarItem(
                        selected = currentTab == AppTab.JOURNAL,
                        onClick = { currentTab = AppTab.JOURNAL },
                        icon = {
                            Icon(
                                if (currentTab == AppTab.JOURNAL) Icons.Filled.CameraAlt else Icons.Outlined.CameraAlt,
                                contentDescription = "Journal"
                            )
                        },
                        label = { Text("Journal", fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = SunriseGold,
                            indicatorColor = ElectricPurple,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("tab_journal")
                    )
                }
            },
            containerColor = DeepMidnight,
            modifier = modifier
        ) { paddingValues ->
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tabContent"
            ) { tab ->
                when (tab) {
                    AppTab.ALARMS -> {
                        AlarmsScreen(
                            alarms = alarms,
                            todayChallenge = todayChallenge,
                            streakCount = records.size,
                            onToggleAlarm = { viewModel.toggleAlarm(it) },
                            onSaveAlarm = { viewModel.addOrUpdateAlarm(it) },
                            onDeleteAlarm = { viewModel.deleteAlarm(it) },
                            onStartTestMission = { alarm ->
                                val target = alarm?.danceTargetSeconds ?: todayChallenge.targetDurationSeconds
                                viewModel.selectChallenge(todayChallenge)
                                viewModel.startMission(alarm?.label ?: "Dance Routine Test", targetSeconds = target)
                            },
                            onScheduleQuickAlarm = { sec ->
                                viewModel.triggerQuickTestAlarm(sec)
                            },
                            modifier = Modifier.padding(paddingValues)
                        )
                    }

                    AppTab.CHALLENGES -> {
                        ChallengesScreen(
                            completions = completions,
                            onSelectAndStartChallenge = { challenge ->
                                viewModel.selectChallenge(challenge)
                                viewModel.startMission("Practice: ${challenge.name}", targetSeconds = challenge.targetDurationSeconds)
                            },
                            modifier = Modifier.padding(paddingValues)
                        )
                    }

                    AppTab.JOURNAL -> {
                        JournalScreen(
                            records = records,
                            onDeleteRecord = { viewModel.deleteWakeRecord(it) },
                            modifier = Modifier.padding(paddingValues)
                        )
                    }
                }
            }
        }
    }
}
