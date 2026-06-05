package com.example.stillzeit.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MealDetailScreen(
    mealId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.v("Meal Id", "$mealId")
    Column(horizontalAlignment = Alignment.Start, modifier = modifier) {
        Text(text = "Meal Detail Screen for Meal ID: $mealId", modifier = modifier)
    }
}