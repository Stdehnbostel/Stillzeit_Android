package com.example.stillzeit.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
data class Feeding @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: Int,
    var volume: Int,
    var type: String,
    // @ForeignKey var mealId: Uuid
)
