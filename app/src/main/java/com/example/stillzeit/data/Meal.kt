package com.example.stillzeit.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import java.util.UUID

@Entity(tableName = "meal")
data class MealInDb (
    @PrimaryKey val uuid: UUID,
    var numberOfFeedings: Int,
    var type: String,
    var start: String,
    var end: String?,
    var comment: String?,
    // var babyId: Uuid?,
)

data class Meal (
    val uuid: UUID,
    var numberOfFeedings: Int,
    var type: String,
    var start: String,
    var end: String?,
    var comment: String?,

    var breastFeedings: List<BreastFeeding>,
    // var bottleFeedings: List<BottleFeeding>,
    // var otherFeedings: List<OtherFeeding>,
)