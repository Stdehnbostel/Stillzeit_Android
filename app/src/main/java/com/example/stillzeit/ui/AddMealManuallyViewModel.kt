package com.example.stillzeit.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stillzeit.StillzeitApplication
import com.example.stillzeit.data.BottleFeeding
import com.example.stillzeit.data.BottleFeedingDetailEntity
import com.example.stillzeit.data.BottleType
import com.example.stillzeit.data.BreastFeeding
import com.example.stillzeit.data.BreastFeedingDetailEntity
import com.example.stillzeit.data.FeedingEntity
import com.example.stillzeit.data.Meal
import com.example.stillzeit.data.MealType
import com.example.stillzeit.data.MealsRepository
import com.example.stillzeit.data.Side
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.UUID

enum class MealTypePage {
    MENU,
    BREAST,
    BOTTLE,
    OTHER,
    PUMPING,
}

class AddMealManuallyViewModel(val repository: MealsRepository) : ViewModel() {
    var page by mutableStateOf(MealTypePage.MENU)
        private set

    var start: ZonedDateTime by mutableStateOf(ZonedDateTime.now())
        private set
    var duration: kotlin.time.Duration by mutableStateOf(kotlin.time.Duration.ZERO)
        private set

    var volume: Int by mutableStateOf(0)
        private set

    fun setFeedingStart(start: ZonedDateTime) {
        this.start = start
        Log.i("FeedingStart", "setFeedingStart: $start")
    }

    fun setFeedingDuration(duration: kotlin.time.Duration) {
        this.duration = duration
        Log.i("Duration", "setFeedingDuration: $duration")
    }

    fun setFeedingVolume(volume: Int) {
        this.volume = volume
        Log.i("Volume", "setVolume: $volume")
    }

    fun setAddMealPage(page: MealTypePage) {
        Log.i("MealTypePage", "setAddMealPage: $page")
        this.page = page
    }

    fun getAddMealPage(): MealTypePage {
        return page
    }

    fun saveBreastFeeding() {
        val end = start.plusMinutes(duration.inWholeMinutes)
        duration = kotlin.time.Duration.ZERO
        val mealUuid = UUID.randomUUID()
        val feedingUuid = UUID.randomUUID()
        val meal = Meal(
            mealUuid,
            1,
            MealType.BREAST,
            repository.createTimeStringForDatabase(start),
            repository.createTimeStringForDatabase(end),
            "",
            listOf(
                BreastFeeding(
                    feeding = FeedingEntity(
                        feedingUuid.toString(),
                        0,
                        MealType.BREAST,
                        repository.createTimeStringForDatabase(ZonedDateTime.now()),
                        mealUuid
                    ),
                    detail = BreastFeedingDetailEntity(
                        feedingUuid.toString(),
                        repository.createTimeStringForDatabase(start),
                        repository.createTimeStringForDatabase(end),
                        true,
                        Side.EMPTY
                    )
                )
            )
        )

        viewModelScope.launch {
            repository.addMeal(meal)
        }
    }

    fun saveBottleFeeding() {
        val mealId = UUID.randomUUID()
        val feedingUuid = UUID.randomUUID()
        val bottleVolume = volume
        volume = 0
        val meal = Meal(
            mealId,
            1,
            MealType.BOTTLE,
            start = repository.createTimeStringForDatabase(start),
            end = null,
            comment = "",
            listOf(),
            listOf(
                BottleFeeding(
                    feeding = FeedingEntity(
                        feedingUuid.toString(),
                        bottleVolume,
                        MealType.BOTTLE,
                        repository.createTimeStringForDatabase(ZonedDateTime.now()),
                        mealId
                    ),
                    detail = BottleFeedingDetailEntity(
                        feedingUuid.toString(),
                        repository.createTimeStringForDatabase(start),
                        BottleType.MILK,
                    )
                )
            )
        )

        viewModelScope.launch {
            repository.addMeal(meal)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as StillzeitApplication)
                val mealsRepository = application.container.mealsRepository
                AddMealManuallyViewModel(repository = mealsRepository)
            }
        }
    }
}
