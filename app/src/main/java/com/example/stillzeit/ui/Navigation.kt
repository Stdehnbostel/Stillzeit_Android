package com.example.stillzeit.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    ADD_MEALS("addmeals", "Add Meals", Icons.Default.Add, "Add Meals"),
    PROTOCOL("protocl", "Protocol", Icons.Default.List, "Protocol"),
}