package com.example.posturev2.notif

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.posturev2.usecase.HourRangeUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltWorker
class NotifWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var hourRangeUseCase: HourRangeUseCase

    @Inject
    lateinit var postureManager: PostureNotifManager

    override suspend fun doWork(): Result {
        if (hourRangeUseCase.isInUserHourRange(System.currentTimeMillis()))  {
            postureManager.sendNotification()
        }

        return Result.success()
    }

    companion object {
        const val TAG = "Notification_Worker_Tag"
    }
}