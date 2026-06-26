package com.example.stillzeit

import com.example.stillzeit.data.Meal
import com.example.stillzeit.data.MealsRepository
import com.example.stillzeit.data.Side
import com.example.stillzeit.ui.AddMealViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import java.time.ZonedDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

/**
 * Local unit tests, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
class TimeStringUnitTests {
    private lateinit var viewModel: AddMealViewModel

    class FakeMealsRepository : MealsRepository {
        override fun getMeals(): Flow<List<Meal>> = flowOf(emptyList())
        override suspend fun addMeal(meal: Meal) {}
        override fun getMealById(mealId: UUID): Flow<Meal> = flowOf()
        override fun incrementWeek() {}
        override fun decrementWeek() {}
        override fun createTimeStringForDatabase(zonedDateTime: ZonedDateTime): String {
            val locale = Locale.GERMANY
            val formatter = DateTimeFormatter
                .ISO_INSTANT
                .withLocale(locale)
            return zonedDateTime.format(formatter)
        }
        override fun getEndOfWeek(): ZonedDateTime = ZonedDateTime.now()
    }

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = AddMealViewModel(FakeMealsRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun createTimeStringForDatabaseFunctionReturnsCorrectlyFormatedStrings() {
        val now = viewModel.getSystemTimeStamp()
        val timeString = viewModel.repository.createTimeStringForDatabase(now)
        val format = Regex("^\\d\\d\\d\\d-(0[1-9]|1[0-2])-([0-3][01]|[0-2]\\d)T([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\dZ\$")

        assertTrue(format.matches(timeString))
    }


    @Test
    fun createTimeStringCorrectlyFormatedStrings() {
        val now = viewModel.getSystemTimeStamp()
        val timeString = viewModel.createTimeString(now)

        val format = Regex("^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$")
        assertTrue(format.matches(timeString))
    }

    @Test
    fun createDurationStringGeneratesCorrectlyFormatedString() {
        val zone = ZoneId.of("Europe/Berlin")
        val start = ZonedDateTime.of(2025, 8, 1, 10, 0, 0, 0, zone)
        val end = ZonedDateTime.of(2025, 8, 1, 11, 9, 20, 0, zone)

        val format = Regex("^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$")
        val durationString = viewModel.createDurationString(start, end, Side.EMPTY)
        assertTrue(format.matches(durationString))
    }
}