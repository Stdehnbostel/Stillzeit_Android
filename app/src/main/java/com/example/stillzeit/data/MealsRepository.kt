package com.example.stillzeit.data

import kotlinx.coroutines.flow.Flow

interface MealsRepository {
    fun getMeals(): Flow<List<Meal>>
    suspend fun addMeal(meal: Meal)
}