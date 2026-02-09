package com.stillzeit.android.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.stillzeit.android.model.enums.MealType

@Entity(
    tableName = "meal",
    indices = [Index(value = ["uuid"], unique = true)]
)
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "number_of_feedings")
    val numberOfFeedings: Long,

    @ColumnInfo(name = "type")
    val type: MealType,

    @ColumnInfo(name = "start")
    val start: Long,

    @ColumnInfo(name = "end")
    val end: Long? = null,

    @ColumnInfo(name = "last_modified")
    val lastModified: Long,

    @ColumnInfo(name = "comment")
    val comment: String? = null,

    @ColumnInfo(name = "baby_id")
    val babyId: String? = null
)
