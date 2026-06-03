package com.yashwant.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.yashwant.model.HistoryItem
import com.yashwant.model.ProfileState
import kotlinx.coroutines.tasks.await

class PrefManager { // Class name is the same!

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Helper to get the logged-in User's ID
    private val uid: String? get() = auth.currentUser?.uid

    // =========================================================
    // THEME
    // =========================================================

    fun saveTheme(isDark: Boolean) {
        uid?.let { id ->
            db.collection("users").document(id)
                .set(mapOf("theme" to isDark), SetOptions.merge())
        }
    }

    // Changing this to 'suspend' because internet is not instant
    suspend fun loadTheme(): Boolean {
        val id = uid ?: return true
        return try {
            val snapshot = db.collection("users").document(id).get().await()
            snapshot.getBoolean("theme") ?: true
        } catch (e: Exception) {
            true
        }
    }

    // =========================================================
    // HISTORY (CALCULATOR)
    // =========================================================

    fun saveHistory(history: List<HistoryItem>) {
        uid?.let { id ->
            // Firestore saves Lists directly as Arrays
            db.collection("users").document(id)
                .set(mapOf("history" to history), SetOptions.merge())
        }
    }

    suspend fun loadHistory(): List<HistoryItem> {
        val id = uid ?: return emptyList()
        return try {
            val snapshot = db.collection("users").document(id).get().await()
            val list = snapshot.get("history") as? List<Map<String, Any>> ?: return emptyList()

            list.map {
                HistoryItem(
                    expression = it["expression"] as? String ?: "",
                    result = it["result"] as? String ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // =========================================================
    // PROFILE
    // =========================================================

    fun saveProfile(state: ProfileState) {
        uid?.let { id ->
            db.collection("users").document(id)
                .set(mapOf("profile" to state), SetOptions.merge())
        }
    }

    suspend fun loadProfile(): ProfileState {
        val id = uid ?: return ProfileState()
        return try {
            val snapshot = db.collection("users").document(id).get().await()
            val data = snapshot.get("profile") as? Map<String, Any> ?: return ProfileState()

            ProfileState(
                name = data["name"] as? String ?: "",
                email = data["email"] as? String ?: "",
                phone = data["phone"] as? String ?: "",
                role = data["role"] as? String ?: "",
                location = data["location"] as? String ?: ""
            )
        } catch (e: Exception) {
            ProfileState()
        }
    }

    // =========================================================
    // CLEANUP
    // =========================================================

    fun clearProfile() {
        uid?.let { id ->
            db.collection("users").document(id)
                .update("profile", null)
        }
    }

    fun clearAll() {
        uid?.let { id ->
            db.collection("users").document(id).delete()
        }
    }
}