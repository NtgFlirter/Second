package com.yashwant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey val name: String = "",
    val price: Double = 0.0,
    val image: String? = null,
    var quantity: Int = 1,
    val restaurantName: String = "Waves Kitchen"
)