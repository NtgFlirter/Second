package com.yashwant.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.yashwant.model.CartItem
import com.yashwant.model.HistoryItem
import com.yashwant.model.OrderItem
import com.yashwant.model.ProfileState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class UserRepository(
    val settingsManager: SettingsManager,
    private val context: android.content.Context? = null
)  {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Current Logged-in User ki ID nikalne ke liye
    val uid: String? get() = auth.currentUser?.uid

    // UI hamesha isko observe karega
    val themeStream: Flow<Boolean> = settingsManager.themeFlow

    suspend fun toggleTheme(isDark: Boolean) {
        // Step A: Local Store mein save karo
        settingsManager.saveTheme(isDark)

        // Step B: Firebase par update kardo
        uid?.let { id ->
            db.collection("users").document(id)
                .set(mapOf("isDarkTheme" to isDark), SetOptions.merge())
        }
    }


    suspend fun signUp(email: String, pass: String): Result<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(email, pass).await()
            // Naya user bante hi uska default document Firestore mein bana do
            uid?.let { id ->
                db.collection("users").document(id).set(
                    mapOf("createdAt" to System.currentTimeMillis()),
                    SetOptions.merge()
                )
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getProfileStream(): Flow<ProfileState> = callbackFlow {
        val id = uid ?: return@callbackFlow

        // addSnapshotListener keeps a live connection to the document
        val listener = db.collection("users").document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                val data = snapshot?.get("profile") as? Map<String, Any>
                if (data != null) {
                    val profile = ProfileState(
                        name = data["name"] as? String ?: "",
                        email = data["email"] as? String ?: "",
                        phone = data["phone"] as? String ?: "",
                        role = data["role"] as? String ?: "",
                        location = data["location"] as? String ?: ""
                    )
                    trySend(profile) // This sends the data to the ViewModel instantly
                }
            }

        // Removed listener when not needed to save battery/data
        awaitClose { listener.remove() }
    }

    suspend fun login(email: String, pass: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    // UserRepository.kt ke andar ye function paste karein:

    suspend fun placeOrder(order: OrderItem): Result<Boolean> {
        // 1. Check user login
        val id = uid ?: return Result.failure(Exception("User not logged in"))

        return try {
            // 2. Order ko 'orders' collection mein save kiya (Global access ke liye)
            db.collection("orders").document(order.orderId).set(order).await()

            // 3. ✨ JADU: Cart ko khali karna (Order successful hone ke baad)
            // User ke personal cart folder mein jitne bhi items hain, unhe delete karo
            val cartItems = db.collection("users").document(id).collection("cart").get().await()
            for (document in cartItems.documents) {
                document.reference.delete().await()
            }

            Result.success(true)
        } catch (e: Exception) {
            // Agar internet chala jaye ya koi error aaye
            Result.failure(e)
        }
    }


    suspend fun saveProfile(profile: ProfileState) {
        uid?.let { id ->
            db.collection("users").document(id)
                .set(mapOf("profile" to profile), SetOptions.merge())
        }
    }

    fun getOrdersStream(): Flow<List<OrderItem>> = callbackFlow {
        val id = uid ?: return@callbackFlow
        val listener = db.collection("orders")
            .whereEqualTo("userId", id) // Sirf usi user ke orders dikhao
            .addSnapshotListener { snap, _ ->
                val list = snap?.toObjects(OrderItem::class.java) ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
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

    suspend fun saveHistory(history: List<HistoryItem>) {
        uid?.let { id ->
            db.collection("users").document(id)
                .set(mapOf("history" to history), SetOptions.merge())
        }
    }

    // 1. Cart mein item daalne ke liye
    suspend fun addToCart(item: CartItem) {
        uid?.let { id ->
            // Hum food name ko hi document ID bana rahe hain taaki duplicate na ho
            db.collection("users").document(id)
                .collection("cart").document(item.name)
                .set(item)
        }
    }

    // 2. Cart ke items Live dekhne ke liye (Stream)
    fun getCartStream(): Flow<List<CartItem>> = callbackFlow {
        val id = uid ?: return@callbackFlow
        val listener = db.collection("users").document(id).collection("cart")
            .addSnapshotListener { snapshot, _ ->
                val items = snapshot?.toObjects(CartItem::class.java) ?: emptyList()
                trySend(items) // ViewModel ko data bhej do
            }
        awaitClose { listener.remove() } // Memory saaf karo
    }

    // 3. Item delete karne ke liye
    suspend fun deleteFromCart(itemName: String) {
        uid?.let { id ->
            db.collection("users").document(id)
                .collection("cart").document(itemName).delete()
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
}