package com.example.stillzeit.data

sealed interface MealComponent : Comparable<MealComponent>{
    val id: String?
    val intervallTime: Pair<String, String?>
    val title: String
    val description: String
    val start: String
    val volume: Int
    val mealId: String
}