package com.yashwant.data

import androidx.room.*
import com.yashwant.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: CartItem): Long

    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItem>>

    @Delete
    suspend fun deleteItem(item: CartItem): Int

    @Query("DELETE FROM cart_items")
    suspend fun clearCart(): Int
}