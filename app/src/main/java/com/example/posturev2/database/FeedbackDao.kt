package com.example.posturev2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedbackDao {

    @Query("Select * FROM feedback")
    fun getFeedbacks(): Flow<List<Feedback>>

    @Insert
    suspend fun insertFeedback(feedback: Feedback)

}