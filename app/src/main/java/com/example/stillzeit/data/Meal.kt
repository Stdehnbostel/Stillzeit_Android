package com.example.stillzeit.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import java.util.UUID

enum class MealType(value: String) {
    BREAST("breast"),
    BOTTLE("bottle"),
    OTHER("other"),
    PUMPING("pumping")
}

data class Meal (
    val uuid: UUID,
    var numberOfFeedings: Int,
    var type: String,
    var start: String,
    var end: String?,
    var comment: String?,

    var breastFeedings: List<BreastFeeding> = emptyList(),
    // var bottleFeedings: List<BottleFeeding> = emptyList(),
    // var otherFeedings: List<OtherFeeding> = emptyList(),
    // var pumpings: List<Pumping> = emptyList()
)