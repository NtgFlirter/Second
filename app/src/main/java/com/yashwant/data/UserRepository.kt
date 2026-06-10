package com.yashwant.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.yashwant.model.AddressState
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
    private val context: Context? = null
)  {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val uid: String? get() = auth.currentUser?.uid

    val themeStream: Flow<Boolean> = settingsManager.themeFlow

    // Robust check for actual internet connectivity
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        // NET_CAPABILITY_VALIDATED ensures the internet is actually working, not just "connected"
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
               capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    suspend fun toggleTheme(isDark: Boolean) {
        settingsManager.saveTheme(isDark)
        uid?.let { id ->
            db.collection("users").document(id)
                .set(mapOf("isDarkTheme" to isDark), SetOptions.merge())
        }
    }

    suspend fun signUp(email: String, pass: String): Result<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(email, pass).await()
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
                    trySend(profile)
                }
            }
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


    // 1. Save Address
    suspend fun saveAddress(address: AddressState) {
        uid?.let { id ->
            db.collection("users").document(id)
                .set(mapOf("address" to address), SetOptions.merge()).await()
        }
    }

    // 2. Load Address (One-time fetch)
    suspend fun loadAddress(): AddressState {
        val id = uid ?: return AddressState()
        return try {
            val snapshot = db.collection("users").document(id).get().await()
            val data = snapshot.get("address") as? Map<String, Any> ?: return AddressState()
            AddressState(
                zipCode = data["zipCode"] as? String ?: "",
                country = data["country"] as? String ?: "",
                state = data["state"] as? String ?: "",
                city = data["city"] as? String ?: "",
                street = data["street"] as? String ?: "",
                addressType = data["addressType"] as? String ?: "Home"
            )
        } catch (e: Exception) { AddressState() }
    }

    /**
     * Places an order. Uses runTransaction to ensure it only succeeds if online.
     * Prevents multiple "ghost" orders being queued in local cache while offline.
     */
    suspend fun placeOrder(order: OrderItem): Result<Boolean> {
        val id = uid ?: return Result.failure(Exception("User not logged in"))
        
        // 1. Double check connectivity before starting
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("PLEASE CHECK YOUR INTERNET CONNECTION"))
        }

        return try {
            // 2. runTransaction forces a server check. It cannot complete offline.
            db.runTransaction { transaction ->
                val orderRef = db.collection("orders").document(order.orderId)
                transaction.set(orderRef, order)
            }.await()
            
            // 3. Clear cart from Firestore only after successful transaction
            val cartItems = db.collection("users").document(id).collection("cart").get().await()
            for (document in cartItems.documents) {
                document.reference.delete().await()
            }
            Result.success(true)
        } catch (e: Exception) {
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
        var firestoreListener: ListenerRegistration? = null
        trySend(emptyList())

        fun startListening(userId: String) {
            firestoreListener?.remove()
            firestoreListener = db.collection("orders")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snap, error ->
                    if (error != null) {
                        trySend(emptyList())
                        return@addSnapshotListener
                    }
                    val list = snap?.toObjects(OrderItem::class.java) ?: emptyList()
                    trySend(list)
                }
        }

        val initialUid = auth.currentUser?.uid
        if (initialUid != null) startListening(initialUid)

        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) startListening(userId) else {
                firestoreListener?.remove()
                trySend(emptyList())
            }
        }

        auth.addAuthStateListener(authListener)
        awaitClose { 
            auth.removeAuthStateListener(authListener)
            firestoreListener?.remove()
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

    suspend fun saveHistory(history: List<HistoryItem>) {
        uid?.let { id ->
            db.collection("users").document(id)
                .set(mapOf("history" to history), SetOptions.merge())
        }
    }

    suspend fun addToCart(item: CartItem) {
        uid?.let { id ->
            db.collection("users").document(id)
                .collection("cart").document(item.name)
                .set(item)
        }
    }

    fun getCartStream(): Flow<List<CartItem>> = callbackFlow {
        val id = uid ?: return@callbackFlow
        val listener = db.collection("users").document(id).collection("cart")
            .addSnapshotListener { snapshot, _ ->
                val items = snapshot?.toObjects(CartItem::class.java) ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }

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