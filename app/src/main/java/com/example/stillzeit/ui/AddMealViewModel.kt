package com.example.stillzeit.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.stillzeit.data.BreastFeeding
import com.example.stillzeit.data.BreastFeedingDetail
import com.example.stillzeit.data.Feeding
import com.example.stillzeit.data.Meal
import kotlinx.coroutines.flow.StateFlow
import kotlin.uuid.ExperimentalUuidApi
import java.util.UUID
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddMealViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddMealUiState(null))

    val uiState: StateFlow<AddMealUiState> = _uiState.asStateFlow()

    fun endFeeding(feeding: Feeding, detail: BreastFeedingDetail) {
        val breastFeeding = BreastFeeding(feeding, detail)
        addFeedingToMeal(breastFeeding)
    }

    fun getLastFeedingSite(): String? {
        val breastFeedings = _uiState.value.meal?.breastFeedings
        if (breastFeedings != null) {
            val lastFeeding = breastFeedings[breastFeedings.size - 1]
            return lastFeeding.detail.side
        } else {
            return null
        }
    }

    fun addFeedingToMeal(feeding: BreastFeeding) {
        _uiState.update {
            currentState ->
            val newMeal = currentState.meal
            if (newMeal != null) {
                newMeal.breastFeedings = newMeal.breastFeedings + listOf(feeding)
                newMeal.numberOfFeedings += 1
                currentState.copy(
                    meal = newMeal
                )
            } else {
                currentState.copy()
            }
        }
        val end = feeding.detail.end
        if (end != null) {
            setMealEnd(end)
        }
    }

    fun setMealStart(mealStart: String) {
        _uiState.update {
            currentState ->
            val oldMeal = currentState.meal
            currentState.copy(
                meal = oldMeal
                    ?: Meal(
                        UUID.randomUUID(),
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