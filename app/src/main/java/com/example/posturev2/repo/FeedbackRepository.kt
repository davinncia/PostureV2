package com.example.posturev2.repo

import com.example.posturev2.database.Feedback
import com.example.posturev2.database.FeedbackDao
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class FeedbackRepository @Inject constructor(private val feedbackDao: FeedbackDao) {

    val feedbacks = feedbackDao.getFeedbacks()

    suspend fun insert(feedback: Feedback) {
        feedbackDao.insertFeedback(feedback)
    }

    suspend fun getFeedbacks(fromTime: Long) = feedbackDao.getFeedbacks(fromTime)

    suspend fun getLastFeedbacks(count: Int = 1) = feedbackDao.getLastFeedback(count)
}