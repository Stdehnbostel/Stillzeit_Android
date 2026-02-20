package com.example.stillzeit.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.stillzeit.R
import com.example.stillzeit.data.Meal

@Composable
fun ProtocolScreen() {
    MealList(
        meals = listOf(),
        modifier = Modifier
    )
}

@Composable
fun MealList(
    meals: List<Meal>,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(meals) {
            meal -> MealCard(meal, Modifier)
        }
    }
}

@Composable
fun MealCard(
    meal: Meal,
    modifier: Modifier
) {
    Text(
        text = stringResource(R.string.meal_description, meal.formattedStartTime(), meal.formattedEndTime(), meal.formattedDuration()),
        modifier = modifier
    )
}