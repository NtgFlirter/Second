package com.yashwant.data

import android.content.Context
import com.yashwant.model.FoodItem
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class FoodRepository(private val context: Context) {

    private val foodDao = AppDatabase.getDatabase(context).foodDao()

    private val restaurantNames = listOf(
        "Hangawi", "Blue Ribbon Sushi", "Cafe Habana", "Blue Ribbon Fried Chicken",
        "Dirty Bird to Go", "Tamarind TriBeCa", "The Meatball Shop", "Barbounia",
        "Anjappar Chettinad", "Bukhara Grill", "Big Wong Restaurant", "Empanada Mama",
        "Pylos", "Lucky's Famous Burgers", "Shake Shack", "RedFarm Hudson",
        "Five Guys Burgers", "Tortaria", "Cafe Mogador", "Otto Enoteca Pizzeria",
        "Vezzo Thin Crust Pizza", "Sushi of Gari", "The Kati Roll Company", "Klong"
    )

    // 1. Add OkHttpClient with timeouts to prevent forever hanging
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    val menuStream: Flow<List<FoodItem>> = foodDao.getAllFood()

    suspend fun refreshMenu() {
        try {
            val response = apiService.getAllRecipes()
            
            if (response.recipes.isNotEmpty()) {
                val mappedItems = response.recipes.map { apiDish ->
                    val nameLength = apiDish.name.length
                    val stablePrice = 10.0 + (nameLength % 10) + (apiDish.cuisine.length % 5)
                    val stablePrepTime = 15 + (nameLength % 15)
                    val restaurantIndex = nameLength % restaurantNames.size
                    val stableRestaurant = restaurantNames[restaurantIndex]

                    FoodItem(
                        name = apiDish.name,
                        price = stablePrice,
                        image = apiDish.image,
                        rating = apiDish.rating,
                        preparationTime = stablePrepTime,
                        cuisine = apiDish.cuisine,
                        restaurantName = stableRestaurant,
                        description = "Enjoy the authentic taste of ${apiDish.name} from $stableRestaurant."
                    )
                }
                foodDao.insertAll(mappedItems)
                println("DEBUG: Menu successfully synced to Room DB")
            }
        } catch (e: Exception) {
            println("DEBUG: Sync failed: ${e.message}")
            // Don't rethrow, just let the offline data be used
        }
    }
}