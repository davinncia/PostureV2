package com.example.posturev2.usecase

import com.example.posturev2.repo.DataStoreRepository
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

class HourRangeUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepository
) {

    suspend fun isInUserHourRange(epoch: Long): Boolean {
        val start = dataStoreRepo.hourRangeStart.first()
        val end = dataStoreRepo.hourRangeEnd.first()

        val cal = Calendar.getInstance().apply { time = Date(epoch) }
        val currentHour = cal.get(Calendar.HOUR_OF_DAY)

        return currentHour in start until end
    }

}