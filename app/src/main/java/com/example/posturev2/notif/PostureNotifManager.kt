package com.example.posturev2.notif

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.posturev2.MainActivity
import com.example.posturev2.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostureNotifManager @Inject constructor(@ApplicationContext val appContext: Context) {

    private val vibrationPattern = longArrayOf(200, 200)

    fun sendNotification() {
        createNotificationChannel(appContext)

        val notificationManager = ContextCompat.getSystemService(
            appContext,
            NotificationManager::class.java
        ) as NotificationManager

        prepareNotification(appContext, notificationManager)
    }

    private fun prepareNotification(
        applicationContext: Context,
        notificationManager: NotificationManager
    ) {

        // On click intent
        val clickIntent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            clickIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

/*
        // Broadcast intent (actions)
        fun getBroadcastIntent(postureResponse: Int): Intent =
            Intent(applicationContext, NotificationReceiver::class.java).apply {
                putExtra("response", postureResponse)
                putExtra("id", NOTIFICATION_ID)
            }

        fun getBroadcastPendingIntent(postureResponse: Int, requestCode: Int) =
            PendingIntent.getBroadcast(
                applicationContext,
                requestCode,
                getBroadcastIntent(postureResponse),
                PendingIntent.FLAG_ONE_SHOT
            )

 */


        // Builder
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            //.setSound(Uri.parse("android.resource://"+appContext.packageName +"/"+R.raw.sound))
            //.setVibrate(longArrayOf(0, 100, 100, 100, 200, 200))
            .setVibrate(vibrationPattern)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(appContext.getString(R.string.notif_title_standing_straight))
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_check,
                appContext.getString(R.string.notif_action_positive),
                //getBroadcastPendingIntent(PostureLog.POSITIVE, POS_RC)
                contentPendingIntent
            )
            .addAction(
                R.drawable.ic_check,
                appContext.getString(R.string.notif_action_negative),
                //getBroadcastPendingIntent(PostureLog.NEGATIVE, NEG_RC)
                contentPendingIntent
            )
            .addAction(
                R.drawable.ic_check,
                "?",
                //getBroadcastPendingIntent(PostureLog.NEUTRAL, NEU_RC)
                contentPendingIntent
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    //------------------------------------- C H A N N E L ----------------------------------------//

    private fun createNotificationChannel(context: Context) {
        //This should always be executed before sending notification
        // Create the NotificationChannel, but only on API 26+
        val notifDescription = "Notification to ask about current posture"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Custom sound
            //Needs to be reinstalled to change notif sound ! Or change channel ID(?)
                /*
            val sound = Uri.parse("android.resource://"+appContext.packageName +"/"+R.raw.sound)
            val soundAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

                 */

            val name: CharSequence = CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = notifDescription
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            //channel.setSound(sound, soundAttributes)
            channel.vibrationPattern = vibrationPattern
            // Register the channel with the system; you can't change the importance or other notification behaviors after this
            val notificationManager: NotificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 0
        private const val CHANNEL_ID = "stand_straight_recall"
        private const val CHANNEL_NAME = "Stand straight"
        private const val POS_RC = 0
        private const val NEG_RC = 1
        private const val NEU_RC = 2

        private var INSTANCE: PostureNotifManager? = null
        fun getInstance(appContext: Context): PostureNotifManager {
            if (INSTANCE == null) {
                synchronized(PostureNotifManager) {
                    if (INSTANCE == null) {
                        INSTANCE = PostureNotifManager(appContext)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}