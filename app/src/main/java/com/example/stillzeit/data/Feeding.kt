package com.example.stillzeit.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlin.uuid.ExperimentalUuidApi
import java.util.UUID

@Entity(foreignKeys = [ForeignKey(
    entity = MealInDb::class, onDelete = ForeignKey.CASCADE,
    parentColumns = ["uuid"],
    childColumns = ["mealId"],
)])
data class Feeding (
    @PrimaryKey val id: UUID,
    var volume: Int,
    var type: String,
    @ColumnInfo(index = true) var mealId: UUID
)
