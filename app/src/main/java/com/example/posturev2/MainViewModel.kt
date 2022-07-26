package com.example.posturev2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.posturev2.notif.NotifWorker
import com.example.posturev2.notif.PostureNotifManager
import com.example.posturev2.repo.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val notifManager: PostureNotifManager,
    private val workManager: WorkManager,
    private val dataStoreRepo: DataStoreRepository
) : ViewModel() {

    private val _isNotifScheduled = MutableStateFlow<Boolean>(false)
    val isNotifScheduled: StateFlow<Boolean> = _isNotifScheduled

    val interval = dataStoreRepo.notifInterval.map {
        "Reminder every $it min"
    }
    val score = dataStoreRepo.weeklyScore.map {
        "$it %"
    }

    init {
        checkNotifIsScheduled()
    }

    fun saveNotifInterval(min: Int = 10) {
        viewModelScope.launch {
            dataStoreRepo.setNotifInterval(min)
        }
    }

    //-------------------------------- W O R K    M A N A G E R ----------------------------------//

    fun scheduleNotif() {
        cancelNotif()

        viewModelScope.launch {
            dataStoreRepo.notifInterval.collect { interval ->
                val notifWorkRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<NotifWorker>(interval.toLong(), TimeUnit.MINUTES)
                    .setInitialDelay(interval.toLong(), TimeUnit.MINUTES)
                    .addTag(NotifWorker.TAG)
                    .build()
                workManager.enqueueUniquePeriodicWork(NotifWorker.TAG, ExistingPeriodicWorkPolicy.REPLACE, notifWorkRequest)
            }
        }

        _isNotifScheduled.value = true
    }

    fun scheduleOneTimeNotif() {
        val notifWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<NotifWorker>().build()
        workManager.enqueue(notifWorkRequest)
    }

    fun cancelNotif() {
        workManager.cancelAllWorkByTag(NotifWorker.TAG)
        _isNotifScheduled.value = false
    }

    private fun checkNotifIsScheduled() {
        viewModelScope.launch {
            workManager.getWorkInfosByTag(NotifWorker.TAG).await().forEach {
                if (it.state == WorkInfo.State.ENQUEUED) {
                    _isNotifScheduled.emit(true)
                    return@launch
                }
                _isNotifScheduled.emit(false)
            }
        }
    }
}