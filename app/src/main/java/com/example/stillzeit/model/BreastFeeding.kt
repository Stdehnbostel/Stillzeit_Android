package com.example.stillzeit.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "breastFeeding")
data class BreastFeeding(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "start") val start: String?,
    @ColumnInfo(name = "end") val end: String?,
    @ColumnInfo(name = "active") val active: Int,
)
