package com.example.stillzeit.ui

import android.util.Log
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class MealDetailViewModel(
    savedStateHandle: SavedStateHandle,
    val repository: MealsRepository
) : ViewModel() {
    val mealId: String = checkNotNull(savedStateHandle["mealId"])

    private val _uiState = MutableStateFlow(MealDetailUiState(meal = null))
    val uiState: StateFlow<MealDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getMealById(UUID.fromString(mealId)).collect { meal ->
                _uiState.update { it.copy(meal = meal) }
            }
        }
    }

    fun startEditingComment() {
        _uiState.update { state ->
            val updatedComment = state.meal?.comment ?: ""
            state.copy(
                isEditing = true,
                updatedComment = updatedComment
            )
        }
    }

    fun updateCommentDraft(text: String) {
        _uiState.update { state ->
            state.copy(
                updatedComment = text
            )
        }
    }

    fun cancelEditing() {
        _uiState.update { state ->
            state.copy(
                isEditing = false
            )
        }
    }

    fun save() {
        Log.i("SaveComment", "save: ${_uiState.value.updatedComment}")
        val meal = uiState.value.meal ?: run {
            _uiState.update { state ->
                state.copy(
                    isEditing = false
                )
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { state ->
                val updatedComment = state.updatedComment
                state.copy(
                    meal = meal.copy(comment = updatedComment),
                    isEditing = false
                )
            }
            val meal = _uiState.value.meal ?: return@launch
            repository.updateMeal(meal)
        }
    }

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
    val meal: Meal?,
    val isEditing: Boolean = false,
    val updatedComment: String = ""
)