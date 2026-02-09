package com.stillzeit.android.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "other_feeding",
    foreignKeys = [
        ForeignKey(
            entity = FeedingEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["feeding_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OtherFeedingDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "feeding_id")
    val feedingId: String,

    @ColumnInfo(name = "start")
    val start: Long,

    @ColumnInfo(name = "description")
    val description: String? = null
)
