package com.example.stillzeit.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "breastFeeding", foreignKeys = [
    ForeignKey(
        entity = Feeding::class,
        parentColumns = ["id"],
        childColumns = ["feedingId"]
    )
])
data class BreastFeedingDetail(
    @PrimaryKey val uid: Int,
    val start: String,
    val end: String?,
    var active: Boolean,
    val side: String,
    val feedingId: Int
)

data class BreastFeeding(
    @Embedded
    var feeding: Feeding,
    @Relation(
        parentColumn = "id",
        entityColumn = "feedingId"
    )
    var detail: BreastFeedingDetail
)