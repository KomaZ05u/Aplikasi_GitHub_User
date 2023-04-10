package com.example.aplikasigithubuser.setting

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences

class settingpref private constructor(private val dataStore: DataStore<Preferences>){
    private val themeKey = booleanPreferencesKey("theme_setting")
    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: settingpref? = null

        fun getInstance(dataStore: DataStore<androidx.datastore.preferences.core.Preferences>): settingpref {
            return INSTANCE ?: synchronized(this) {
                val instance = settingpref(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}