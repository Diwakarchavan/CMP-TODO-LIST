package io.jadu.todoApp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Theme mode: Auto (follows system), Dark, or Light.
 * - null = Auto (follows system theme)
 * - true = Dark mode forced
 * - false = Light mode forced
 */
class ThemeRepository(private val dataStore: DataStore<Preferences>) {

    private val darkModeKey = booleanPreferencesKey("dark_mode")

    val isDarkMode: Flow<Boolean?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            preferences[darkModeKey]
        }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[darkModeKey] = enabled
        }
    }

    /**
     * Cycles through theme states: Auto (null) → Dark (true) → Light (false) → Auto
     */
    suspend fun cycleTheme() {
        dataStore.edit { preferences ->
            val current = preferences[darkModeKey]
            when (current) {
                null -> preferences[darkModeKey] = true
                true -> preferences[darkModeKey] = false
                false -> preferences.remove(darkModeKey)
            }
        }
    }

    /**
     * Reset to Auto mode (follows system theme)
     */
    suspend fun resetToAuto() {
        dataStore.edit { preferences ->
            preferences.remove(darkModeKey)
        }
    }

}