package com.yashwant.model

data class FoodItem(
    val name: String = "",
    val price: Double = 0.0,
    val image: Any? = null,
    val rating: Double = 0.0,
    val preparationTime: Int = 0,
    val cuisine: String = "",
    val restaurantName: String = "",
    val description: String = ""
)