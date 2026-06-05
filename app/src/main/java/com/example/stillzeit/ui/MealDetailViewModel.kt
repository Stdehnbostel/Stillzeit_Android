package com.example.stillzeit.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stillzeit.StillzeitApplication
import com.example.stillzeit.data.Meal
import com.example.stillzeit.data.MealsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

class MealDetailViewModel(
    savedStateHandle: SavedStateHandle,
    val repository: MealsRepository
) : ViewModel() {
    val mealId: String = checkNotNull(savedStateHandle["mealId"])

    val uiState: StateFlow<MealDetailUiState> = repository.getMealById(UUID.fromString(mealId))
        .map { MealDetailUiState(meal = it) }
        .stateIn(
            viewModelScope, 
            SharingStarted.WhileSubscribed(5000), 
            MealDetailUiState(meal = null)
        )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as StillzeitApplication)
                val savedStateHandle = createSavedStateHandle()
                val mealsRepository = application.container.mealsRepository

                MealDetailViewModel(
                    savedStateHandle = savedStateHandle,
                    repository = mealsRepository
                )
            }
        }
    }
}

data class MealDetailUiState(
    val meal: Meal?
)