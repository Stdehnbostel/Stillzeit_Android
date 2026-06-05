package com.example.stillzeit.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MealDetailScreen(
    mealId: String,
    onBack: () -> Unit,
    viewModel: MealDetailViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()
    Column(horizontalAlignment = Alignment.Start, modifier = modifier) {
        // Platzhalter für richtige Detail-Ansicht.
        Text(text = "Meal Detail Screen for Meal ID: $mealId", modifier = modifier)
        Text(text = "Von ${uiState.value.meal?.start ?: "nicht vorhanden"}")
        Text(text = "Bis ${uiState.value.meal?.end ?: "nicht vorhanden"}")
    }
}