package com.example.posturev2.notif

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.posturev2.usecase.UpdateNotifIntervalUseCase
import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject lateinit var useCase: UpdateNotifIntervalUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        val feedback = intent.getBooleanExtra(PostureNotifManager.EXTRA_FEEDBACK, true)

        useCase.update(feedback)

        // Clear notif
        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
        notificationManager.cancel(intent.getIntExtra(PostureNotifManager.ID_NOTIF, 0))
    }
}