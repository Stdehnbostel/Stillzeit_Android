package com.example.stillzeit.data

import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

interface MealsRepository {
    fun getMeals(): Flow<List<Meal>>
    suspend fun addMeal(meal: Meal)

    fun incrementWeek()
    fun decrementWeek()
    fun createTimeStringForDatabase(zonedDateTime: ZonedDateTime): String

    fun getEndOfWeek(): ZonedDateTime
}