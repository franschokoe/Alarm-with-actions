package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.model.WakeUpRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WakeUpRecordDao {
    @Query("SELECT * FROM wake_up_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<WakeUpRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: WakeUpRecordEntity): Long

    @Query("DELETE FROM wake_up_records WHERE id = :id")
    suspend fun deleteRecord(id: Long)

    @Query("SELECT COUNT(*) FROM wake_up_records")
    fun getRecordCount(): Flow<Int>
}
