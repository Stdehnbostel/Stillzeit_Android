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
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    ADD_MEALS("addmeals", R.string.record_meal, "Add Meals", Icons.Default.Add, "Add Meals"),
    PROTOCOL("protocl", R.string.overview, "Protocol", Icons.Default.List, "Protocol"),
}