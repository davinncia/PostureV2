package com.example.posturev2.notif

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class NotifWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    @Inject
    lateinit var postureManager: PostureNotifManager

    override fun doWork(): Result {
        postureManager.sendNotification()

        return Result.success()
    }

    companion object {
        const val TAG = "Notification_Worker_Tag"
    }
}