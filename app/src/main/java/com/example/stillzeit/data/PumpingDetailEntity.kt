package com.example.stillzeit.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "pumping", foreignKeys = [
    ForeignKey(
        entity = FeedingEntity::class,
        parentColumns = ["id"],
        childColumns = ["feedingId"]
    )
])
data class PumpingDetailEntity(
    @PrimaryKey val feedingId: String,
    val start: String,
    val side: Side,
)
