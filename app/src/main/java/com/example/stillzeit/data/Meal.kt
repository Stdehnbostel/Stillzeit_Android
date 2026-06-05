package com.example.stillzeit.data

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID
import java.time.Duration
import java.time.format.DateTimeFormatter
import kotlin.time.toKotlinDuration

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
) {
    fun duration(): kotlin.time.Duration {
        val end = end?.let { ZonedDateTime.parse(it) } ?: ZonedDateTime.now()
        val start = ZonedDateTime.parse(start)
        return Duration.between(start, end).toKotlinDuration()
    }

    fun formattedDuration(): String {
        val duration = duration()
        val hours = duration.inWholeHours
        val minutes = duration.inWholeMinutes % 60
        val seconds = duration.inWholeSeconds % 60
        val postFix = if (hours > 0) "h" else if (minutes > 0) "min" else "s"
        val hoursString = if (hours > 0) "$hours:" else ""
        val minutesString = if (minutes > 0 || hours > 0) "$minutes:" else ""
        val secondsString = if (hours == 0L && minutes > 0 && seconds < 10) "0$seconds" else if (hours == 0L) "$seconds" else ""

        return "$hoursString$minutesString$secondsString $postFix"
    }

    fun formattedStartTime(): String {
        val startTime = ZonedDateTime.parse(start)
        return startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun formattedEndTime(): String {
        val endTime: ZonedDateTime? = end?.let { ZonedDateTime.parse(it) } ?: null
        return endTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""

    }

    fun toMealEntity(): MealEntity {
        return MealEntity(
            uuid = uuid,
            numberOfFeedings = numberOfFeedings,
            type = type,
            start = start,
            end = end,
            lastModified = ZonedDateTime.now().toString(),
            comment = comment,
            babyId = null
        )
    }
}

fun MealEntity.toMeal(breastFeedings: List<BreastFeeding> = listOf()): Meal {
    return Meal(
        uuid = uuid,
        numberOfFeedings = numberOfFeedings,
        type = type,
        start = start,
        end = end,
        comment = comment,
        breastFeedings = breastFeedings
    )
}