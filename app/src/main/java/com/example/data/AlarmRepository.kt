package com.example.data

import com.example.model.AlarmEntity
import com.example.model.ChallengeCompletionEntity
import com.example.model.WakeUpRecordEntity
import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val database: AppDatabase) {
    val allAlarms: Flow<List<AlarmEntity>> = database.alarmDao().getAllAlarms()
    val enabledAlarms: Flow<List<AlarmEntity>> = database.alarmDao().getEnabledAlarms()
    val allRecords: Flow<List<WakeUpRecordEntity>> = database.wakeUpRecordDao().getAllRecords()
    val recordCount: Flow<Int> = database.wakeUpRecordDao().getRecordCount()
    val allChallengeCompletions: Flow<List<ChallengeCompletionEntity>> = database.challengeDao().getAllCompletions()
    val totalChallengeCount: Flow<Int> = database.challengeDao().getTotalCompletedCount()

    suspend fun getAlarmById(id: Long): AlarmEntity? = database.alarmDao().getAlarmById(id)

    suspend fun insertAlarm(alarm: AlarmEntity): Long = database.alarmDao().insertAlarm(alarm)

    suspend fun updateAlarm(alarm: AlarmEntity) = database.alarmDao().updateAlarm(alarm)

    suspend fun deleteAlarm(alarm: AlarmEntity) = database.alarmDao().deleteAlarm(alarm)

    suspend fun deleteAlarmById(id: Long) = database.alarmDao().deleteAlarmById(id)

    suspend fun insertWakeUpRecord(record: WakeUpRecordEntity): Long =
        database.wakeUpRecordDao().insertRecord(record)

    suspend fun deleteWakeUpRecord(id: Long) = database.wakeUpRecordDao().deleteRecord(id)

    suspend fun insertChallengeCompletion(completion: ChallengeCompletionEntity): Long =
        database.challengeDao().insertCompletion(completion)

    suspend fun deleteChallengeCompletion(id: Long) =
        database.challengeDao().deleteCompletion(id)

    fun getChallengeCompletionCount(challengeId: String): Flow<Int> =
        database.challengeDao().getCompletionCountByChallenge(challengeId)
}
