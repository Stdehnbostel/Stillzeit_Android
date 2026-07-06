package com.example.stillzeit.data

import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime
import java.util.UUID

interface MealsRepository {
    fun getMeals(): Flow<List<Meal>>
    suspend fun addMeal(meal: Meal)

    fun getMealById(mealId: UUID): Flow<Meal>

    fun incrementWeek()
    fun decrementWeek()
    fun createTimeStringForDatabase(zonedDateTime: ZonedDateTime): String
    fun getLastMeal(): Flow<Meal?>

    fun getEndOfWeek(): ZonedDateTime

    fun getDays(): Flow<List<Day>>
}

data class Day(
    val meals: List<Meal>,
    val start: ZonedDateTime,
    val end: ZonedDateTime,
)