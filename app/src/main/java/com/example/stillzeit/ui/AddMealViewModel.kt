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
    private val _uiState = MutableStateFlow(AddMealUiState(null))

    val uiState: StateFlow<AddMealUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalUuidApi::class)
    fun setMealStart(mealStart: String) {
        _uiState.update {
            currentState ->
            val oldMeal = currentState.meal
            currentState.copy(
                meal = oldMeal
                    ?: Meal(
                        Uuid.random(),
                        0,
                        "breast",
                        mealStart,
                        "",
                        "oldMeal.comment",
                        listOf()
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
                meal = if (oldMeal != null) {
                    Meal(
                        oldMeal.uuid,
                        oldMeal.numberOfFeedings,
                        oldMeal.type,
                        oldMeal.start,
                        mealEnd,
                        oldMeal.comment,
                        oldMeal.breastFeedings
                    )
                } else {
                    oldMeal
                }
            )
        }
    }

    fun endMeal() {
        _uiState.update {
            currentState -> currentState.copy(
                meal = null
            )
        }
    }
}

data class AddMealUiState(
    val meal: Meal?
)