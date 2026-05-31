package com.example.stillzeit.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MealDetailScreen(
    mealId: String,
    onBack: () -> Unit
) {
    Text(text = "Meal Detail Screen for Meal ID: $mealId")
}