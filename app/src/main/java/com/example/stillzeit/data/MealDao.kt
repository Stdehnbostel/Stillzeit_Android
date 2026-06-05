package com.example.stillzeit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.UUID

@Dao
interface MealDao {
    @Insert
    suspend fun insert(meal: MealEntity)

    @Query("SELECT * FROM meal WHERE babyId = :babyId")
    suspend fun getAllMealsForBaby(babyId: UUID): List<MealEntity>

    @Query("SELECT * FROM meal WHERE babyId IS NULL")
    suspend fun getAllMealsWithoutBabyProfile(): List<MealEntity>

    @Query("SELECT * FROM meal WHERE babyId = :babyId AND (start BETWEEN :start AND :end)")
    suspend fun getMealsForBabyForSelectedTimeframe(babyId: UUID, start: String, end: String): List<MealEntity>

    @Query("SELECT * FROM meal WHERE babyId IS NULL AND (start BETWEEN :start AND :end)")
    suspend fun getMealsWithoutBabyForSelectedTimeframe(start: String, end: String): List<MealEntity>

    @Query("SELECT * FROM meal WHERE uuid = :mealId")
    suspend fun getMealById(mealId: UUID): MealEntity?
}