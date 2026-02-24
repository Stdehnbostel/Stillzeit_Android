package com.example.stillzeit.data

import java.util.UUID

data class Baby(
    val id: UUID,
    val name: String,
    val birthDate: String,
    val gender: String,
    var meals: List<Meal>
)
