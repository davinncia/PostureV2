package com.example.posturev2.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.posturev2.notif.PostureNotifManager
import com.example.posturev2.repo.DataStoreRepository
import com.example.posturev2.repo.FeedbackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val feedbackRepo: FeedbackRepository
) : ViewModel() {

    val message = feedbackRepo.feedbacks.map { feedbacks ->
        var text = ""
        for (i in feedbacks.lastIndex downTo 0) {
            text += "${getReadableTime(feedbacks[i].timeStamp)}  ----  ${feedbacks[i].isStraight}\n"
        }
        return@map text
    }

    private fun getReadableTime(epoch: Long): String? {
        return try {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val netDate = Date(epoch)
            sdf.format(netDate)
        } catch (e: Exception) {
            epoch.toString()
        }
    }
}