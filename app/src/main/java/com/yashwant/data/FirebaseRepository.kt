package com.yashwant.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Get the current user's ID
    private val userId: String?
        get() = auth.currentUser?.uid

    // Save theme to Firestore
    fun saveTheme(isDark: Boolean) {
        val id = userId ?: return
        val data = mapOf("isDarkTheme" to isDark)
        db.collection("users").document(id).set(data)
    }

    // 1. Profile Save karne ke liye
    fun saveUserProfile(name: String, bio: String, github: String) {
        val id = auth.currentUser?.uid ?: return
        val userProfile = mapOf(
            "name" to name,
            "bio" to bio,
            "github" to github
        )
        db.collection("users").document(id).update(userProfile)
    }

    // 2. Profile Data fetch karne ke liye
    suspend fun getUserProfile(): Map<String, Any>? {
        val id = auth.currentUser?.uid ?: return null
        return try {
            val snapshot = db.collection("users").document(id).get().await()
            snapshot.data
        } catch (e: Exception) {
            null
        }
    }

    // Listen to theme changes in real-time
    fun getThemeFlow(): Flow<Boolean> = callbackFlow {
        val id = userId

        if (id == null) {
            trySend(false)
            close()
            return@callbackFlow
        }

        val listener = db.collection("users").document(id)
            .addSnapshotListener { snapshot, _ ->
                val isDark = snapshot?.getBoolean("isDarkTheme") ?: false
                trySend(isDark)
            }

        awaitClose {
            listener.remove()
        }
    }
}
