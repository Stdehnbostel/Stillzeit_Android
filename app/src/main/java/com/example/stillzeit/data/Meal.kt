package com.example.stillzeit.data

import androidx.room.Entity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "meal")
data class MealInDb @OptIn(ExperimentalUuidApi::class) constructor(
    val uuid: Uuid,
    var numberOfFeedings: Int,
    var type: String,
    var start: String,
    var end: String?,
    var comment: String?,
    // var babyId: Uuid?,
)

data class Meal @OptIn(ExperimentalUuidApi::class) constructor(
    val uuid: Uuid,
    var numberOfFeedings: Int,
    var type: String,
    var start: String,
    var end: String?,
    var comment: String?,

    var breastFeedings: List<BreastFeeding>,
    // var bottleFeedings: List<BottleFeeding>,
    // var otherFeedings: List<OtherFeeding>,
)