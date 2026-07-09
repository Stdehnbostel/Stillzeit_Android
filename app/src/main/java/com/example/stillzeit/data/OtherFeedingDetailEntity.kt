package com.example.stillzeit.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "other_feeding", foreignKeys = [
    ForeignKey(
        entity = FeedingEntity::class,
        parentColumns = ["id"],
        childColumns = ["feedingId"]
    )
])
data class OtherFeedingDetailEntity(
    @PrimaryKey val feedingId: String,
    val start: String,
    val description: String?
)
