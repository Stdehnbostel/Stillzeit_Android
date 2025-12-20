package com.example.stillzeit.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breastFeeding")
data class BreastFeedingDetail(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "start") val start: String?,
    @ColumnInfo(name = "end") val end: String?,
    @ColumnInfo(name = "active") val active: Int,
)

data class BreastFeeding(
    var feeding: Feeding,
    var detail: BreastFeedingDetail
)