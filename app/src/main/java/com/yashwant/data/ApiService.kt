package com.yashwant.data

import retrofit2.http.GET

data class FoodResponse(
    val recipes: List<FoodApiItem>
)

data class FoodApiItem(
    val name: String,
    val cuisine: String,
    val image: String,
    val rating: Double
)

interface ApiService {
    @GET("recipes")
    suspend fun getAllRecipes(): FoodResponse
}