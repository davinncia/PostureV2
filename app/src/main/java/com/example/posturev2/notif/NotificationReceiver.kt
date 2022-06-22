package com.example.posturev2.notif

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.posturev2.database.Feedback
import com.example.posturev2.database.PostureDatabase
import com.example.posturev2.repo.FeedbackRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject lateinit var feedbackRepo: FeedbackRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return
        if (intent.action != Intent.ACTION_INPUT_METHOD_CHANGED) return

        val feedback = intent.getBooleanExtra(PostureNotifManager.EXTRA_FEEDBACK, true)

        CoroutineScope(Dispatchers.IO).launch {
            feedbackRepo.insert(Feedback(System.currentTimeMillis(), feedback))
        }

        // Clear notif
        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
        notificationManager.cancel(intent.getIntExtra(PostureNotifManager.ID_NOTIF, 0))
    }
}