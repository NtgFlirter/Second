package com.yashwant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_menu") // Table ka naam
data class FoodItem(
    @PrimaryKey val name: String = "",
    val price: Double = 0.0,
    val image: String = "",
    val rating: Double = 0.0,
    val preparationTime: Int = 0,
    val cuisine: String = "",
    val restaurantName: String = "",
    val description: String = ""
)