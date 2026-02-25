package com.example.stillzeit.data

import android.util.Log
import androidx.compose.runtime.currentComposer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalMealsRepository(
    private val mealDao: MealDao,
    private val breastFeedingDao: BreastFeedingDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : MealsRepository {
    private val _meals = MutableStateFlow<List<Meal>>(listOf())

    init {
        scope.launch {
            val mealEntities = mealDao.getAllMealsWithoutBabyProfile()
            _meals.value = mealEntities.map {
                    it.toMeal(breastFeedingDao.getFeedingsForMeal(it.uuid))
            }
        }
    }

    override fun getMeals(): Flow<List<Meal>> = _meals

    override suspend fun addMeal(meal: Meal) {
        _meals.update { currentMeals -> currentMeals + meal }
        mealDao.insert(meal.toMealEntity())
        for (feeding in meal.breastFeedings) {
            breastFeedingDao.insert(feeding)
        }
    }
}