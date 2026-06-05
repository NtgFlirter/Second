package com.yashwant.data

import com.yashwant.model.FoodItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoodRepository {

    // 1. List of your 24 Restaurants
    private val restaurantNames = listOf(
        "Hangawi", "Blue Ribbon Sushi", "Cafe Habana", "Blue Ribbon Fried Chicken",
        "Dirty Bird to Go", "Tamarind TriBeCa", "The Meatball Shop", "Barbounia",
        "Anjappar Chettinad", "Bukhara Grill", "Big Wong Restaurant", "Empanada Mama",
        "Pylos", "Lucky's Famous Burgers", "Shake Shack", "RedFarm Hudson",
        "Five Guys Burgers", "Tortaria", "Cafe Mogador", "Otto Enoteca Pizzeria",
        "Vezzo Thin Crust Pizza", "Sushi of Gari", "The Kati Roll Company", "Klong"
    )

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    private var cachedMenu: List<FoodItem> = emptyList()

    suspend fun getApiMenu(): List<FoodItem> {
        if (cachedMenu.isNotEmpty()) return cachedMenu

        return try {
            val response = apiService.getAllRecipes()

            // 2. Mapping Logic with ALL parameters
            val mappedItems = response.recipes.map { apiDish ->

                // STABLE LOGIC (Naam ke hisaab se pricing)
                val nameLength = apiDish.name.length
                val stablePrice = 10.0 + (nameLength % 10) + (apiDish.cuisine.length % 5)
                val stablePrepTime = 15 + (nameLength % 15) // 15 se 30 min ke beech

                // Restaurant name ko bhi stable rakhte hain (Naam ke first character ke basis par)
                val restaurantIndex = nameLength % restaurantNames.size
                val stableRestaurant = restaurantNames[restaurantIndex]

                FoodItem(
                    name = apiDish.name,
                    price = stablePrice,
                    image = apiDish.image, // URL String getting from aip
                    rating = apiDish.rating,
                    preparationTime = stablePrepTime,
                    cuisine = apiDish.cuisine,
                    restaurantName = stableRestaurant,
                    description = "Enjoy the authentic taste of ${apiDish.name} from $stableRestaurant."
                )
            }

            cachedMenu = mappedItems
            mappedItems
        } catch (e: Exception) {
            emptyList()
        }
    }
}