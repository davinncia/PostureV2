package com.example.posturev2

import androidx.lifecycle.ViewModel
import com.example.posturev2.notif.PostureNotifManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val notifManager: PostureNotifManager
) : ViewModel() {

    fun sendNotif() {
        notifManager.sendNotification()
    }
}