package com.example.stillzeit.ui

import androidx.lifecycle.ViewModel
import com.example.stillzeit.data.Meal

class AddMealViewModel : ViewModel() {
}

data class AddMealUiState(
    val meal: Meal
)