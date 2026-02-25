package com.example.stillzeit.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "feeding",
    foreignKeys = [ForeignKey(
    entity = MealEntity::class, onDelete = ForeignKey.CASCADE,
    parentColumns = ["uuid"],
    childColumns = ["mealId"],
)])
data class FeedingEntity (
    @PrimaryKey val id: String,
    var volume: Int? = null,
    var type: MealType,
    @ColumnInfo(index = true) var mealId: UUID
)
