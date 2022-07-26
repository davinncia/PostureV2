package com.example.posturev2.usecase

import androidx.work.*
import com.example.posturev2.database.Feedback
import com.example.posturev2.notif.NotifWorker
import com.example.posturev2.repo.DataStoreRepository
import com.example.posturev2.repo.FeedbackRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.roundToInt


class UpdateNotifIntervalUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepository,
    private val feedbackRepo: FeedbackRepository,
    private val workManager: WorkManager
) {

    fun update(feedback: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            feedbackRepo.insert(Feedback(System.currentTimeMillis(), feedback))

            val score = computeWeekPercent()
            val interval = computeRecallsInterval(score)
            dataStoreRepo.updateWeeklyProgress(score)
            dataStoreRepo.setNotifInterval(interval)

            // Set new notif
            workManager.cancelAllWorkByTag(NotifWorker.TAG)

            val notifWorkRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<NotifWorker>(interval.toLong(), TimeUnit.MINUTES)
                .setInitialDelay(interval.toLong(), TimeUnit.MINUTES)
                .addTag(NotifWorker.TAG)
                .build()
            workManager.enqueueUniquePeriodicWork(NotifWorker.TAG, ExistingPeriodicWorkPolicy.REPLACE, notifWorkRequest)
        }

    }

    private suspend fun computeWeekPercent(): Int {
        var score = 0
        var count = 0
        val lastWeek = System.currentTimeMillis() - (1_000 * 60 * 60 * 24 * 7)

        feedbackRepo.getFeedbacks(fromTime = lastWeek).forEach {
            count++
            if (it.isStraight) score++
        }

        return if(count == 0) 0 else ((score / count.toDouble()) * 100).roundToInt()
    }

    // f(x) = 0.8x + 20
    private fun computeRecallsInterval(scorePercent: Int) = (0.8F * scorePercent + 20).roundToInt()
}