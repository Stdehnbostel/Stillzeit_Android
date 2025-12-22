package com.example.stillzeit.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.stillzeit.data.Meal
import kotlinx.coroutines.flow.StateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddMealViewModel : ViewModel() {
    @OptIn(ExperimentalUuidApi::class)
    private val _uiState = MutableStateFlow(AddMealUiState(Meal(
        uuid = Uuid.random(),
        numberOfFeedings = 0,
        type = "breast",
        start = "",
        end = "",
        comment = "",
        breastFeedings = listOf(),
    )))

    val uiState: StateFlow<AddMealUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalUuidApi::class)
    fun setMealStart(mealStart: String) {
        _uiState.update {
            currentState ->
            val oldMeal = currentState.meal
            currentState.copy(
                meal = Meal(
                    oldMeal.uuid,
                    oldMeal.numberOfFeedings,
                    oldMeal.type,
                    mealStart,
                    oldMeal.end,
                    oldMeal.comment,
                    oldMeal.breastFeedings
                )
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun setMealEnd(mealEnd: String) {
        _uiState.update {
                currentState ->
            val oldMeal = currentState.meal
            currentState.copy(
                meal = Meal(
                    oldMeal.uuid,
                    oldMeal.numberOfFeedings,
                    oldMeal.type,
                    oldMeal.start,
                    mealEnd,
                    oldMeal.comment,
                    oldMeal.breastFeedings
                )
            )
        }
    }
}

data class AddMealUiState(
    val meal: Meal
)