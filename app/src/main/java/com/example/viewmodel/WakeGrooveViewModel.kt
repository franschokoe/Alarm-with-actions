package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm.AlarmAudioService
import com.example.alarm.AlarmScheduler
import com.example.data.AlarmRepository
import com.example.data.AppDatabase
import com.example.model.AlarmEntity
import com.example.model.ChallengeCompletionEntity
import com.example.model.DanceChallenge
import com.example.model.DanceChallengeRepository
import com.example.model.WakeUpRecordEntity
import com.example.sensor.DanceMotionDetector
import com.example.sensor.DanceMotionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class WakeMissionStage {
    READY,
    DANCE_CHALLENGE,
    AWAKE_SELFIE,
    CELEBRATION
}

class WakeGrooveViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlarmRepository
    private val scheduler: AlarmScheduler

    val allAlarms: StateFlow<List<AlarmEntity>>
    val allRecords: StateFlow<List<WakeUpRecordEntity>>
    val allCompletions: StateFlow<List<ChallengeCompletionEntity>>

    // Daily & Active Challenge
    private val _todayChallenge = MutableStateFlow(DanceChallengeRepository.getTodayChallenge())
    val todayChallenge: StateFlow<DanceChallenge> = _todayChallenge.asStateFlow()

    private val _activeChallenge = MutableStateFlow(DanceChallengeRepository.getTodayChallenge())
    val activeChallenge: StateFlow<DanceChallenge> = _activeChallenge.asStateFlow()

    // Alarm ringing & mission state
    private val _isAlarmRinging = MutableStateFlow(false)
    val isAlarmRinging: StateFlow<Boolean> = _isAlarmRinging.asStateFlow()

    private val _missionStage = MutableStateFlow(WakeMissionStage.READY)
    val missionStage: StateFlow<WakeMissionStage> = _missionStage.asStateFlow()

    private val _danceMotionState = MutableStateFlow(DanceMotionState())
    val danceMotionState: StateFlow<DanceMotionState> = _danceMotionState.asStateFlow()

    private val _capturedSelfiePath = MutableStateFlow<String?>(null)
    val capturedSelfiePath: StateFlow<String?> = _capturedSelfiePath.asStateFlow()

    private val _activeAlarmLabel = MutableStateFlow("Morning Groove")
    val activeAlarmLabel: StateFlow<String> = _activeAlarmLabel.asStateFlow()

    private var motionDetector: DanceMotionDetector? = null

    init {
        val db = AppDatabase.getDatabase(application)
        repository = AlarmRepository(db)
        scheduler = AlarmScheduler(application)

        allAlarms = repository.allAlarms.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allRecords = repository.allRecords.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allCompletions = repository.allChallengeCompletions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        // Prepopulate with a default 7:00 AM energetic alarm if database is empty
        viewModelScope.launch {
            allAlarms.collect { list ->
                if (list.isEmpty()) {
                    val defaultAlarm = AlarmEntity(
                        hour = 7,
                        minute = 0,
                        label = "Rise & Groove Party",
                        isEnabled = true,
                        repeatDays = "2,3,4,5,6", // Weekdays
                        danceTargetSeconds = 15,
                        danceIntensity = "Medium Groove"
                    )
                    val id = repository.insertAlarm(defaultAlarm)
                    scheduler.schedule(defaultAlarm.copy(id = id))
                }
            }
        }
    }

    fun selectChallenge(challenge: DanceChallenge) {
        _activeChallenge.value = challenge
    }

    fun addOrUpdateAlarm(alarm: AlarmEntity) {
        viewModelScope.launch {
            if (alarm.id == 0L) {
                val newId = repository.insertAlarm(alarm)
                scheduler.schedule(alarm.copy(id = newId))
            } else {
                repository.updateAlarm(alarm)
                scheduler.schedule(alarm)
            }
        }
    }

    fun toggleAlarm(alarm: AlarmEntity) {
        val updated = alarm.copy(isEnabled = !alarm.isEnabled)
        viewModelScope.launch {
            repository.updateAlarm(updated)
            if (updated.isEnabled) {
                scheduler.schedule(updated)
            } else {
                scheduler.cancel(alarm)
            }
        }
    }

    fun deleteAlarm(alarm: AlarmEntity) {
        viewModelScope.launch {
            scheduler.cancel(alarm)
            repository.deleteAlarm(alarm)
        }
    }

    fun triggerQuickTestAlarm(seconds: Int = 5) {
        scheduler.scheduleQuickTest(seconds, "Quick Dance Test")
    }

    fun startMission(label: String = "Dance Challenge Mission", targetSeconds: Int = 15) {
        _activeAlarmLabel.value = label
        _missionStage.value = WakeMissionStage.DANCE_CHALLENGE
        _capturedSelfiePath.value = null

        // Initialize motion detector for this mission
        motionDetector?.stop()
        motionDetector = DanceMotionDetector(
            context = getApplication(),
            targetSeconds = targetSeconds
        ) { state ->
            _danceMotionState.value = state
            if (state.progress >= 1.0f && _missionStage.value == WakeMissionStage.DANCE_CHALLENGE) {
                _missionStage.value = WakeMissionStage.AWAKE_SELFIE
            }
        }.also {
            it.start()
        }
    }

    fun simulateDanceStep() {
        motionDetector?.simulateDanceMove(boostFactor = 1.3f)
    }

    fun onSelfieConfirmed(filePath: String) {
        _capturedSelfiePath.value = filePath
        _missionStage.value = WakeMissionStage.CELEBRATION

        // Turn off sound and vibration
        AlarmAudioService.stop(getApplication())
        _isAlarmRinging.value = false
        motionDetector?.stop()

        // Log completion in database
        viewModelScope.launch {
            val challenge = _activeChallenge.value
            val moves = _danceMotionState.value.totalMovesCount
            val duration = _danceMotionState.value.targetSeconds

            // Record in wake-up history
            val record = WakeUpRecordEntity(
                alarmLabel = _activeAlarmLabel.value,
                danceSecondsCompleted = duration,
                danceMovesCount = moves,
                grooveRating = "${challenge.emoji} ${challenge.name} Master!",
                selfieImagePath = filePath,
                isVerified = true
            )
            repository.insertWakeUpRecord(record)

            // Record challenge completion
            val completion = ChallengeCompletionEntity(
                challengeId = challenge.id,
                challengeName = challenge.name,
                challengeEmoji = challenge.emoji,
                durationSeconds = duration,
                selfieImagePath = filePath,
                movesDetected = moves,
                grooveScore = 100
            )
            repository.insertChallengeCompletion(completion)
        }
    }

    fun dismissMission() {
        AlarmAudioService.stop(getApplication())
        motionDetector?.stop()
        _isAlarmRinging.value = false
        _missionStage.value = WakeMissionStage.READY
    }

    fun snoozeAlarm() {
        AlarmAudioService.stop(getApplication())
        motionDetector?.stop()
        _isAlarmRinging.value = false
        _missionStage.value = WakeMissionStage.READY
        // Re-ring in 5 minutes
        scheduler.scheduleQuickTest(300, "Snoozed Groove Alarm")
    }

    fun handleIncomingAlarm(label: String, danceSeconds: Int) {
        _isAlarmRinging.value = true
        // Randomly pick a dance challenge or use today's challenge
        val randomChallenge = DanceChallengeRepository.ALL_CHALLENGES.random()
        _activeChallenge.value = randomChallenge
        startMission(label, targetSeconds = danceSeconds)
    }

    fun deleteWakeRecord(id: Long) {
        viewModelScope.launch {
            repository.deleteWakeUpRecord(id)
        }
    }

    override fun onCleared() {
        motionDetector?.stop()
        super.onCleared()
    }
}
