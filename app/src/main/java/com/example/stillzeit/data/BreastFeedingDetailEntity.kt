package com.example.stillzeit.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "breast_feeding", foreignKeys = [
    ForeignKey(
        entity = FeedingEntity::class,
        parentColumns = ["id"],
        childColumns = ["feedingId"]
    )
])
data class BreastFeedingDetailEntity(
    @PrimaryKey val feedingId: String,
    val start: String,
    val end: String?,
    var active: Boolean,
    val side: Side,
)
