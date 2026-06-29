package com.example.stillzeit.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.room.util.TableInfo

@Composable
fun MealDetailScreen(
    mealId: String,
    onBack: () -> Unit,
    viewModel: MealDetailViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()
    val meal = uiState.value.meal
    Column(horizontalAlignment = Alignment.Start, modifier = modifier) {
        Row(modifier = Modifier) {
            Column(modifier = Modifier) {
                Text("Datum")
                Text(text = meal?.formattedStartDate() ?: "", modifier = Modifier)
            }
            Column(modifier = Modifier) {
                Text("Uhrzeit")
                Text(text = meal?.formattedStartTime() ?: "", modifier = Modifier)
            }
        }

        Row(modifier = Modifier) {
            Column(modifier = Modifier) {
                Text("Dauer")
                Text(text = meal?.formattedDuration() ?: "", modifier = Modifier)
            }
        }


    }
}