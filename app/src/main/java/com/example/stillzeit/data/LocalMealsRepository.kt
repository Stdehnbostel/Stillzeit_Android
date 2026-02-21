package com.example.stillzeit.data

import android.util.Log
import androidx.compose.runtime.currentComposer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LocalMealsRepository: MealsRepository {
    private val _meals = MutableStateFlow<List<Meal>>(emptyList())

    override fun getMeals(): Flow<List<Meal>> = _meals

    override suspend fun addMeal(meal: Meal) {
        Log.i("LocalMealsRepository", "addMeal: $meal")
        _meals.update { currentMeals -> currentMeals + meal }
    }
}