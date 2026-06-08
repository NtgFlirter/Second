package com.yashwant.model

import androidx.annotation.DrawableRes

data class CartItem(
    val name: String = "",
    val price: Double = 0.0,
    val image: Any? = null, // ✅ Changed to Any?
    var quantity: Int = 1,
    val restaurantName: String = "Waves Kitchen"
)