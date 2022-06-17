package com.example.posturev2.repo

import com.example.posturev2.database.Feedback
import com.example.posturev2.database.FeedbackDao
import javax.inject.Inject

class FeedbackRepository @Inject constructor(private val feedbackDao: FeedbackDao) {

    val feedbacks = feedbackDao.getFeedbacks()

    suspend fun insert(feedback: Feedback) {
        feedbackDao.insertFeedback(feedback)
    }

}