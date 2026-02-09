package com.example.stillzeit.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "meal")
data class MealEntity(
    @PrimaryKey val uuid: UUID,
    val numberOfFeedings: Int,
    val type: String,
    val start: String,
    val end: String?,
    val lastModified: String,
    val comment: String?,
    val babyId: String?,
)