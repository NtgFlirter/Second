package com.yashwant.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {
    private val THEME_KEY = booleanPreferencesKey("is_dark_theme")

    // Theme ko read kare
    val themeFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[THEME_KEY] ?: false
    }

    // Theme save kare
    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = isDark
        }
    }
}