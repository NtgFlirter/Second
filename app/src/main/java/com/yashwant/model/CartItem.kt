package com.yashwant.model

import androidx.annotation.DrawableRes

data class CartItem(
    val name: String = "",
    val price: Double = 0.0,
    @DrawableRes val image: Int = 0,
    var quantity: Int = 1,
    val restaurantName: String = "Waves Kitchen"
)