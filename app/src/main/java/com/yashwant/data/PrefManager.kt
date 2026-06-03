package com.yashwant.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yashwant.model.HistoryItem
import com.yashwant.model.ProfileState

class PrefManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("app_pref", Context.MODE_PRIVATE)

    private val gson = Gson()

    // =========================================================
    // THEME
    // =========================================================

    fun saveTheme(isDark: Boolean) {
        prefs.edit().putBoolean("theme", isDark).apply()
    }

    fun loadTheme(): Boolean {
        return prefs.getBoolean("theme", true)
    }

    // =========================================================
    // HISTORY (CALCULATOR)
    // =========================================================

    fun saveHistory(history: List<HistoryItem>) {
        val json = gson.toJson(history)
        prefs.edit()
            .putString("history", json)
            .apply()
    }

    fun loadHistory(): List<HistoryItem> {
        val json = prefs.getString("history", null)
            ?: return emptyList()

        val type = object : TypeToken<List<HistoryItem>>() {}.type

        return gson.fromJson(json, type)
    }

    // =========================================================
    // PROFILE
    // =========================================================

    fun saveProfile(state: ProfileState) {
        prefs.edit()
            .putString("name", state.name)
            .putString("email", state.email)
            .putString("phone", state.phone)
            .putString("role", state.role)
            .putString("location", state.location)
            .apply()
    }

    fun loadProfile(): ProfileState {
        return ProfileState(
            name = prefs.getString("name", "") ?: "",
            email = prefs.getString("email", "") ?: "",
            phone = prefs.getString("phone", "") ?: "",
            role = prefs.getString("role", "") ?: "",
            location = prefs.getString("location", "") ?: ""
        )
    }

    fun clearProfile() {
        prefs.edit()
            .remove("name")
            .remove("email")
            .remove("phone")
            .remove("role")
            .remove("location")
            .apply()
    }

    // =========================================================
    // OPTIONAL: FULL RESET (if needed later)
    // =========================================================

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}