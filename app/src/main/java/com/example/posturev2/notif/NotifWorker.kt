package com.example.posturev2.notif

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotifWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {

        PostureNotifManager.getInstance(applicationContext).sendNotification()

        return Result.success()
    }

}