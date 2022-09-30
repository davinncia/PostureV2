package com.example.posturev2.settings

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posturev2.repo.DataStoreRepository
import com.example.posturev2.usecase.HourRangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepository
) : ViewModel() {

    val hourRange: Flow<ClosedFloatingPointRange<Float>> = dataStoreRepo.hourRangeStart.combine(dataStoreRepo.hourRangeEnd) { start, end ->
        start.toFloat()..end.toFloat()
    }



    private var handler: Handler = Handler(Looper.getMainLooper())
    private var runner: Runnable? = null

    // Using throttle
    fun setHourRange(range: ClosedFloatingPointRange<Float>) {
        runner?.let { handler.removeCallbacks(it) }
        runner = Runnable {
            storeRange(range)
        }.apply {
            handler.postDelayed(this, 300)
        }
    }

    private fun storeRange(range: ClosedFloatingPointRange<Float>) {
        viewModelScope.launch {
            dataStoreRepo.setHourRange(range)
        }
    }
}