package com.example.stillzeit.ui

import androidx.lifecycle.ViewModel
import com.example.stillzeit.data.Meal
import kotlinx.coroutines.flow.StateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.MutableStateFlow

class AddMealViewModel : ViewModel() {
    @OptIn(ExperimentalUuidApi::class)
    private val _uiState = MutableStateFlow(AddMealUiState(Meal(
        uuid = Uuid.random(),
        numberOfFeedings = 0,
        type = "breast",
        start = "2025-01-01T00:00:00Z",
        end = "2025-01-01T00:00:00Z",
        comment = "",
        breastFeedings = listOf(),
    )))

    val uiState: StateFlow<AddMealUiState> = _uiState
}

data class AddMealUiState(
    val meal: Meal
)