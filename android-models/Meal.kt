package com.stillzeit.android.domain.model

import com.stillzeit.android.model.enums.BreastFeedingSide
import com.stillzeit.android.model.enums.MealType
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Domain model corresponding to iOS Meal struct.
 * Contains the meal metadata and lists of all feeding sub-types.
 */
data class Meal(
    val id: Long? = null,
    val uuid: String,
    val numberOfFeedings: Long,
    val type: MealType,
    val start: Instant,
    val end: Instant? = null,
    val lastModified: Instant,
    val comment: String? = null,
    val babyId: String? = null,
    val breastFeedings: List<BreastFeeding> = emptyList(),
    val bottleFeedings: List<BottleFeeding> = emptyList(),
    val otherFeedings: List<OtherFeeding> = emptyList(),
    val pumpings: List<Pumping> = emptyList()
) : Comparable<Meal> {

    override fun compareTo(other: Meal): Int = start.compareTo(other.start)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Meal) return false
        return uuid == other.uuid
    }

    override fun hashCode(): Int = uuid.hashCode()
}

// =============================================================================
// Extension Functions – Display & Formatting
// Correspond to computed properties in iOS Meal.swift
// =============================================================================

private val timeFormatter: DateTimeFormatter
    get() = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        .withZone(ZoneId.systemDefault())

private val dateFormatter: DateTimeFormatter
    get() = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        .withZone(ZoneId.systemDefault())

private val dateTimeFormatter: DateTimeFormatter
    get() = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        .withZone(ZoneId.systemDefault())

// --- Duration ---

/** Duration between start and end (or now). Matches iOS `duration`. */
fun Meal.getDuration(): Duration =
    Duration.between(start, end ?: Instant.now())

/**
 * Formatted duration like "1:05 h", "12:30 min", or "45 s".
 * Matches iOS `formattedDuration`.
 */
fun Meal.getFormattedDuration(): String = formatDuration(getDuration())

// --- Display Type ---

/** Localized type name. Matches iOS `displayType`. */
fun Meal.getDisplayType(): String = when (type) {
    MealType.BREAST -> "Stillen"
    MealType.BOTTLE -> "Flasche"
    MealType.OTHER -> "Beikost"
    MealType.PUMPING -> "Abpumpen"
}

// --- Time Strings ---

/** Formatted start time with date. Matches iOS `startTimeMinutes`. */
fun Meal.getStartTimeMinutes(): String = dateTimeFormatter.format(start)

/** Formatted end time with date, or "noch nicht beendet". Matches iOS `endTimeMinutes`. */
fun Meal.getEndTimeMinutes(): String =
    end?.let { dateTimeFormatter.format(it) } ?: "noch nicht beendet"

/** Only time portion, e.g. "14:30". Matches iOS `formattedTimeOfMeal`. */
fun Meal.getFormattedTimeOfMeal(): String = timeFormatter.format(start)

/**
 * Date range string. If same day, only start date. Matches iOS `formattedDateOfMeal`.
 */
fun Meal.getFormattedDateOfMeal(): String {
    val startStr = dateFormatter.format(start)
    val endStr = end?.let {
        val startDate = LocalDate.ofInstant(start, ZoneId.systemDefault())
        val endDate = LocalDate.ofInstant(it, ZoneId.systemDefault())
        if (startDate == endDate) "" else " - ${dateFormatter.format(it)}"
    } ?: ""
    return "$startStr$endStr"
}

// --- Volume ---

/** Total volume across all feeding types. Matches iOS `volume`. */
fun Meal.getTotalVolume(): Long =
    breastFeedings.sumOf { it.volume ?: 0 } +
        bottleFeedings.sumOf { it.volume ?: 0 } +
        otherFeedings.sumOf { it.volume ?: 0 } +
        pumpings.sumOf { it.volume ?: 0 }

/** Formatted volume with unit (ml or g). Matches iOS `formattedVolume`. */
fun Meal.getFormattedVolume(): String {
    val vol = getTotalVolume()
    return when (type) {
        MealType.OTHER -> "$vol g"
        else -> "$vol ml"
    }
}

// --- Breast Feeding Specifics ---

/** Volume from left-side feedings. Matches iOS `breastFeedingLeftVolume`. */
fun Meal.getLeftVolume(): Long =
    breastFeedings
        .filter { it.side == BreastFeedingSide.LEFT }
        .sumOf { it.volume ?: 0 }

/** Volume from right-side feedings. Matches iOS `breastFeedingRightVolume`. */
fun Meal.getRightVolume(): Long =
    breastFeedings
        .filter { it.side == BreastFeedingSide.RIGHT }
        .sumOf { it.volume ?: 0 }

/** Total feeding duration for left side. Matches iOS `breastFeedingLeftFeedingTime`. */
fun Meal.getLeftFeedingDuration(): Duration {
    val leftFeedings = breastFeedings.filter { it.side == BreastFeedingSide.LEFT }
    if (leftFeedings.isEmpty()) return Duration.ZERO
    return leftFeedings.fold(Duration.ZERO) { acc, feeding ->
        val endTime = feeding.end ?: Instant.now()
        acc.plus(Duration.between(feeding.start, endTime))
    }
}

/** Total feeding duration for right side. Matches iOS `breastFeedingRightFeedingTime`. */
fun Meal.getRightFeedingDuration(): Duration {
    val rightFeedings = breastFeedings.filter { it.side == BreastFeedingSide.RIGHT }
    if (rightFeedings.isEmpty()) return Duration.ZERO
    return rightFeedings.fold(Duration.ZERO) { acc, feeding ->
        val endTime = feeding.end ?: Instant.now()
        acc.plus(Duration.between(feeding.start, endTime))
    }
}

/** Formatted left-side duration. Matches iOS `formattedLeftDuration`. */
fun Meal.getFormattedLeftDuration(): String = formatDuration(getLeftFeedingDuration())

/** Formatted right-side duration. Matches iOS `formattedRightDuration`. */
fun Meal.getFormattedRightDuration(): String = formatDuration(getRightFeedingDuration())

// --- Other Feeding ---

/** Joined descriptions of other feedings. Matches iOS `otherFeedingDescriptions`. */
fun Meal.getOtherFeedingDescriptions(): String =
    otherFeedings.mapNotNull { it.feedingDescription }.joinToString(", ")

// --- Time Since Meal ---

/**
 * Formatted time elapsed since the meal ended (or started for non-breast types).
 * Matches iOS `displayTimeSinceMeal()`.
 */
fun Meal.displayTimeSinceMeal(): String {
    val reference = when (type) {
        MealType.BREAST -> end ?: Instant.now()
        MealType.BOTTLE, MealType.OTHER, MealType.PUMPING -> start
    }
    val duration = Duration.between(reference, Instant.now())
    val hours = duration.toHours()
    val minutes = (duration.toMinutes() % 60).toInt()
    val postfix = if (hours > 0) "Stunden" else "Minuten"
    val hourStr = if (hours == 0L) "" else "$hours:"
    val minuteStr = if (minutes < 10 && hours > 0) "0$minutes" else "$minutes"
    return "$hourStr$minuteStr $postfix"
}

// --- Display Description ---

/**
 * Full display description with start/end/duration/comment.
 * Matches iOS `displayDiscription`.
 */
fun Meal.getDisplayDescription(): String {
    val startStr = DateTimeFormatter.ofPattern("HH:mm:ss")
        .withZone(ZoneId.systemDefault())
        .format(start)

    val endStr: String
    val durationStr: String

    if (end != null) {
        endStr = DateTimeFormatter.ofPattern("HH:mm:ss")
            .withZone(ZoneId.systemDefault())
            .format(end)
        val dur = Duration.between(start, end)
        val h = dur.toHours()
        val m = (dur.toMinutes() % 60).toInt()
        val s = (dur.seconds % 60).toInt()
        val suffix = when {
            h > 0 -> "h"
            m > 0 -> "m"
            else -> "s"
        }
        durationStr = String.format("%02d:%02d:%02d %s", h, m, s, suffix)
    } else {
        endStr = "-"
        durationStr = "noch nicht beendet"
    }

    val fromLabel = if (type == MealType.BREAST) "Von" else "Um"
    val toSection = if (type == MealType.BREAST) ", bis $endStr" else ""
    val durationSection = if (type == MealType.BREAST) ", \nDauer $durationStr" else ""

    return "$fromLabel $startStr$toSection$durationSection\nKommentar: ${comment ?: ""}"
}

// =============================================================================
// Private Helpers
// =============================================================================

private fun formatDuration(duration: Duration): String {
    val hours = duration.toHours()
    val minutes = (duration.toMinutes() % 60).toInt()
    val seconds = (duration.seconds % 60).toInt()

    return when {
        hours > 0 -> {
            val minStr = if (minutes < 10) "0$minutes" else "$minutes"
            "$hours:$minStr h"
        }
        minutes > 0 -> {
            val secStr = if (seconds < 10) "0$seconds" else "$seconds"
            "$minutes:$secStr min"
        }
        else -> "$seconds s"
    }
}
