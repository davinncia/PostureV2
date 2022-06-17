package com.example.posturev2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.posturev2.notif.NotifWorker
import com.example.posturev2.notif.PostureNotifManager
import com.example.posturev2.repo.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val notifManager: PostureNotifManager,
    private val workManager: WorkManager,
    private val dataStoreRepo: DataStoreRepository
) : ViewModel() {

    val interval = dataStoreRepo.notifInterval

    private fun sendNotif() {
        notifManager.sendNotification()
    }

    fun saveNotifInterval(min: Int = 10) {
        viewModelScope.launch {
            dataStoreRepo.setNotifInterval(min)
        }
    }

    //-------------------------------- W O R K    M A N A G E R ----------------------------------//

    fun scheduleNotif() {
        val notifWorkRequest: WorkRequest = PeriodicWorkRequestBuilder<NotifWorker>(15, TimeUnit.MINUTES)
            .setInitialDelay(1, TimeUnit.MINUTES)
            .addTag(NotifWorker.TAG)
            .build()
        workManager.enqueue(notifWorkRequest)
    }

    fun scheduleOneTimeNotif() {
        val notifWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<NotifWorker>().build()
        workManager.enqueue(notifWorkRequest)
    }

    fun cancelNotif() {
        workManager.cancelAllWorkByTag(NotifWorker.TAG)
    }

    fun getNotifWorkerState(): Boolean {
        val info = workManager.getWorkInfosByTag(NotifWorker.TAG)
        return !(info.isCancelled || info.isDone || info.get().isEmpty())
    }
}