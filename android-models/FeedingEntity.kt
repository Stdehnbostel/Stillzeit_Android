package com.stillzeit.android.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.stillzeit.android.model.enums.MealType

@Entity(
    tableName = "feeding",
    foreignKeys = [
        ForeignKey(
            entity = MealEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["meal_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["uuid"], unique = true),
        Index(value = ["meal_id"])
    ]
)
data class FeedingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "volume")
    val volume: Long? = null,

    @ColumnInfo(name = "type")
    val type: MealType,

    @ColumnInfo(name = "meal_id")
    val mealId: String
)
