package com.stillzeit.android.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.stillzeit.android.model.enums.BottleType

@Entity(
    tableName = "bottle_feeding",
    foreignKeys = [
        ForeignKey(
            entity = FeedingEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["feeding_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BottleFeedingDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "feeding_id")
    val feedingId: String,

    @ColumnInfo(name = "start")
    val start: Long,

    @ColumnInfo(name = "type")
    val type: BottleType
)
