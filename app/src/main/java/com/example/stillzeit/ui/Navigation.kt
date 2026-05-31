package com.example.stillzeit.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.stillzeit.R

enum class Destination(
    val route: String,
    @StringRes val titleRes: Int,
    @StringRes val label: Int,
    val icon: ImageVector?,
    val contentDescription: String,
    val showInNavigationBar: Boolean
) {
    ADD_MEALS("addmeals", R.string.record_meal, R.string.record_meal_label, Icons.Default.Add, "Add Meals", true),
    PROTOCOL("protocol", R.string.overview, R.string.overview_label, Icons.Default.List, "Protocol", true),
    MEAL_DETAILS("meal_details/{mealId", R.string.meal_description, 0, null, "", false)
}