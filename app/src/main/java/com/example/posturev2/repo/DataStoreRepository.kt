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

    suspend fun setNotifInterval(minutes: Int) {
        dataStore.edit {  preferences ->
            preferences[PreferenceKeys.NOTIF_TIME_INTERVAL] = minutes
        }
    }

    object PreferenceKeys {
        val NOTIF_TIME_INTERVAL = intPreferencesKey("interval")
    }

    companion object {
        const val PREFERENCES_NAME = "PosturePreferences"
    }
}