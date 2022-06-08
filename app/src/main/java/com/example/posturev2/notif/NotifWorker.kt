package com.example.posturev2.notif

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import javax.inject.Inject

class NotifWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    @Inject lateinit var postureManager: PostureNotifManager

    override fun doWork(): Result {
        postureManager.sendNotification()

        return Result.success()
    }

    companion object {
        const val TAG = "Notification_Worker_Tag"
    }
}