package com.example.stillzeit.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "bottle_feeding", foreignKeys = [
    ForeignKey(
        entity = FeedingEntity::class,
        parentColumns = ["id"],
        childColumns = ["feedingId"]
    )
])
data class BottleFeedingDetailEntity (
    @PrimaryKey val feedingId: String,
    val start: String,
    val type: BottleType
)