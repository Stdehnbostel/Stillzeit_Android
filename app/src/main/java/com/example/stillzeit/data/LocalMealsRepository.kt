package com.example.stillzeit.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

class LocalMealsRepository(
    private val mealDao: MealDao,
    private val breastFeedingDao: BreastFeedingDao,
    private val bottleFeedingDao: BottleFeedingDao,
    private val otherFeedingDao: OtherFeedingDao,
    private val pumpingDao: PumpingDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : MealsRepository {
    // Müsste eigentlich auf das tatsächliche Ende der Woche, bzw. den Anfang der nächsten Woche
    // Montag 0:00 Uhr gesetzt werden.
    private var endOfWeek = getEndOfWeekForTimestamp(ZonedDateTime.now())

    fun getEndOfWeekForTimestamp(timestamp: ZonedDateTime): ZonedDateTime {
        val dayOfWeek = timestamp.dayOfWeek
        val endDay = timestamp.plusDays(8 - dayOfWeek.value.toLong())

        val truncatedEndDay = endDay.truncatedTo(java.time.temporal.ChronoUnit.DAYS)
        Log.i("EndOfWeek:", truncatedEndDay.toString())
        return  endDay.truncatedTo(java.time.temporal.ChronoUnit.DAYS)
    }
    private val _meals = MutableStateFlow<List<Meal>>(listOf())

    private val _days = MutableStateFlow<List<Day>>(listOf())

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
                it.toMeal(
                    breastFeedings = breastFeedingDao.getFeedingsForMeal(it.uuid),
                    bottleFeedings = bottleFeedingDao.getFeedingsForMeal(it.uuid)
                )
            }
            _days.value = makeDays(_meals.value)
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

    override fun getMealById(mealId: UUID): Flow<Meal> = flow {
        val mealEntity = mealDao.getMealById(mealId)
        if (mealEntity != null) {
            val breastFeedings = breastFeedingDao.getFeedingsForMeal(mealId)
            val bottleFeedings = bottleFeedingDao.getFeedingsForMeal(mealId)
            val otherFeedings = otherFeedingDao.getFeedingsForMeal(mealId)
            val pumpings = pumpingDao.getFeedingsForMeal(mealId)
            emit(
                mealEntity.toMeal(breastFeedings, bottleFeedings, otherFeedings, pumpings)
            )
        }
    }.flowOn(Dispatchers.IO)

    override fun getLastMeal(): Flow<Meal?> = flow {
        val mealEntity = mealDao.getLastMeal()
        if (mealEntity != null) {
            val breastFeedings = breastFeedingDao.getFeedingsForMeal(mealEntity.uuid)
            val bottleFeedings = bottleFeedingDao.getFeedingsForMeal(mealEntity.uuid)
            val otherFeedings = otherFeedingDao.getFeedingsForMeal(mealEntity.uuid)
            val pumpings = pumpingDao.getFeedingsForMeal(mealEntity.uuid)
            emit(mealEntity.toMeal(breastFeedings, bottleFeedings))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addMeal(meal: Meal) {
        _meals.update { currentMeals -> currentMeals + meal }
        mealDao.insert(meal.toMealEntity())
        for (feeding in meal.breastFeedings) {
            breastFeedingDao.insert(feeding)
        }
        loadMeals()
    }

    override suspend fun updateMeal(meal: Meal) {
        mealDao.updateMeal(meal.uuid, meal.comment, ZonedDateTime.now().toString())
        loadMeals()
    }

    override fun incrementWeek() {
        // keine zukünftigen Wochen anzeigen.
        if (endOfWeek < getEndOfWeekForTimestamp(ZonedDateTime.now())) {
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

    fun makeDays(meals: List<Meal>): List<Day> {
        var days = mutableListOf<Day>()
        for (weekDay in 0L..7L) {
            val start = endOfWeek.plusDays(-weekDay)
            val end = endOfWeek.plusDays(-weekDay + 1)
            val daysMeals = meals.filter {
                    it.startAsDate() in start..<end
                }
                days.add(Day(daysMeals, start, end))
            }

        Log.i("days", days.toString())
        return days
    }

    override fun getDays(): Flow<List<Day>> {
        return _days
    }
}
