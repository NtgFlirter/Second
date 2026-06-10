package com.yashwant.data

import androidx.room.*
import com.yashwant.model.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_menu")
    fun getAllFood(): Flow<List<FoodItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(foodList: List<FoodItem>): List<Long>

    @Query("DELETE FROM food_menu")
    suspend fun deleteAll(): Int // Return Int to avoid KSP "unexpected jvm signature V" error
}