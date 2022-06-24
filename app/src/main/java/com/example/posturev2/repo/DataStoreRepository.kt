package com.example.posturev2.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val notifInterval = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.NOTIF_TIME_INTERVAL] ?: -1
    }

    val weeklyScore = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.WEEKLY_SCORE] ?: 0
    }

    suspend fun setNotifInterval(minutes: Int) {
        dataStore.edit {  preferences ->
            preferences[PreferenceKeys.NOTIF_TIME_INTERVAL] = minutes
        }
    }

    suspend fun updateWeeklyProgress(scorePercent: Int) {
        dataStore.edit {  preferences ->
            preferences[PreferenceKeys.WEEKLY_SCORE] = scorePercent
        }
    }

    object PreferenceKeys {
        val NOTIF_TIME_INTERVAL = intPreferencesKey("interval")
        val WEEKLY_SCORE = intPreferencesKey("weekly_score")
    }

    companion object {
        const val PREFERENCES_NAME = "PosturePreferences"
    }
}