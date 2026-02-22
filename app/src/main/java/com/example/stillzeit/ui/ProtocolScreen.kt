package com.example.stillzeit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stillzeit.R
import com.example.stillzeit.data.Meal

@Composable
fun ProtocolScreen(
    viewModel: ProtocolViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous Week"
                )
            }
            Text(
                text = stringResource(
                    R.string.date_range,
                    viewModel.getFormattedStartOfSelectedWeek(),
                    viewModel.getFormattedEndOfSelectedWeek()
                ),
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next Week"
                )
            }
        }

        MealList(
            meals = uiState.value.meals,
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun MealList(
    meals: List<Meal>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(meals) {
            meal -> MealCard(
            meal,
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
            )
        }
    }
}

@Composable
fun MealCard(
    meal: Meal,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.meal_description, meal.formattedStartTime(), meal.formattedEndTime(), meal.formattedDuration()),
            modifier = Modifier.padding(16.dp)
        )
    }
}
