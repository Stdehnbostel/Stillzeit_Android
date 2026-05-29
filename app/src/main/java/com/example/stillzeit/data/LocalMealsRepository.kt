package com.example.stillzeit.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class LocalMealsRepository(
    private val mealDao: MealDao,
    private val breastFeedingDao: BreastFeedingDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : MealsRepository {
    // Müsste eigentlich auf das tatsächliche Ende der Woche, bzw. den Anfang der nächsten Woche
    // Montag 0:00 Uhr gesetzt werden.
    private var endOfWeek: ZonedDateTime = if (ZonedDateTime.now().dayOfWeek == DayOfWeek.SUNDAY) {
        ZonedDateTime.now()
    } else {
        ZonedDateTime.now().plusDays(7 - ZonedDateTime.now().dayOfWeek.value.toLong())
    }
    private val _meals = MutableStateFlow<List<Meal>>(listOf())

    init {
        loadMeals()
    }

    private fun loadMeals() {
        scope.launch {
            val mealEntities = mealDao.getMealsWithoutBabyForSelectedTimeframe(
                start = createTimeStringForDatabase(endOfWeek.plusDays(-7)),
                end = createTimeStringForDatabase(endOfWeek)
            )
            _meals.value = mealEntities.map {
                it.toMeal(breastFeedingDao.getFeedingsForMeal(it.uuid))
            }
        }
    }

    override fun createTimeStringForDatabase(zonedDateTime: ZonedDateTime): String {
        // DateTimeFormatter erfordert API leven 26.
        // Da Zeitstemple für diese App von zentraler Bedeutung sind
        // stellt dies die untere Schrnake der zu Unterstützenden
        // Android Versionen dar. (Android 8.0)
        val locale = Locale.GERMANY
        val formatter = DateTimeFormatter
            .ISO_INSTANT
            .withLocale(locale)
        return zonedDateTime.format(formatter)
    }

    override fun getMeals(): Flow<List<Meal>> = _meals

    override suspend fun addMeal(meal: Meal) {
        _meals.update { currentMeals -> currentMeals + meal }
        mealDao.insert(meal.toMealEntity())
        for (feeding in meal.breastFeedings) {
            breastFeedingDao.insert(feeding)
        }
    }

    override fun incrementWeek() {
        // keine zukündtigen Wochen anzeigen.
        if (endOfWeek < ZonedDateTime.now().plusDays(7)) {
            endOfWeek = endOfWeek.plusDays(7)
            loadMeals()
        }
        Log.i("INCWEEK", endOfWeek.toString())
    }

    override fun decrementWeek() {
        endOfWeek = endOfWeek.plusDays(-7)
        loadMeals()
        Log.i("DECWEEK", endOfWeek.toString())
    }

    override fun getEndOfWeek(): ZonedDateTime {
        return endOfWeek
    }
}
