package com.example.stillzeit.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(foreignKeys = [ForeignKey(
    entity = MealInDb::class, onDelete = ForeignKey.CASCADE,
    parentColumns = ["uuid"],
    childColumns = ["mealId"],
)])
data class Feeding @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: Int,
    var volume: Int,
    var type: String,
    @ColumnInfo(index = true) var mealId: Uuid
)
