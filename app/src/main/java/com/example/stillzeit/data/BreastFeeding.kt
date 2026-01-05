package com.example.stillzeit.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

sealed class Side(val side: String) {
    class Left: Side("left")
    class Right: Side("right")
    class Empty: Side("")
}

@Entity(tableName = "breastFeeding", foreignKeys = [
    ForeignKey(
        entity = Feeding::class,
        parentColumns = ["id"],
        childColumns = ["feedingId"]
    )
])
data class BreastFeedingDetail (
    @PrimaryKey val uid: Int,
    val start: String,
    val end: String?,
    var active: Boolean,
    val side: Side,
    val feedingId: UUID,
)

data class BreastFeeding(
    @Embedded
    val feeding: Feeding,
    @Relation(
        parentColumn = "id",
        entityColumn = "feedingId"
    )
    val detail: BreastFeedingDetail
)