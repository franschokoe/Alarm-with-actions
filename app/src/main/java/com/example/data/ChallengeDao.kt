package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.model.ChallengeCompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenge_completions ORDER BY completedAt DESC")
    fun getAllCompletions(): Flow<List<ChallengeCompletionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: ChallengeCompletionEntity): Long

    @Query("SELECT COUNT(*) FROM challenge_completions")
    fun getTotalCompletedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM challenge_completions WHERE challengeId = :challengeId")
    fun getCompletionCountByChallenge(challengeId: String): Flow<Int>

    @Query("DELETE FROM challenge_completions WHERE id = :id")
    suspend fun deleteCompletion(id: Long)
}
