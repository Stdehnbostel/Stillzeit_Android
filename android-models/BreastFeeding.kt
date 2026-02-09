package com.stillzeit.android.domain.model

import com.stillzeit.android.model.enums.BreastFeedingSide
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Domain model combining Feeding + BreastFeedingDetail (flat structure).
 * Corresponds to iOS BreastFeeding struct.
 */
data class BreastFeeding(
    val feedingId: Long? = null,
    val feedingUuid: String,
    override val volume: Long? = null,
    override val mealId: String,
    override val start: Instant,
    val end: Instant? = null,
    val active: Boolean,
    val side: BreastFeedingSide
) : MealComponent {

    override val id: String get() = feedingUuid

    override val timeInterval: Pair<Instant, Instant?>
        get() = start to end

    /** Formatted duration like "5 Minuten" or "1:05 Stunden". Matches iOS `title`. */
    override val title: String
        get() {
            val endTime = end ?: return "noch nicht beendet"
            val duration = Duration.between(start, endTime)
            val hours = duration.toHours()
            val minutes = (duration.toMinutes() % 60).toInt()
            return if (hours > 0) {
                val minStr = if (minutes < 10) "0$minutes" else "$minutes"
                "$hours:$minStr Stunden"
            } else {
                "$minutes Minuten"
            }
        }

    /** Formatted time range like "Von: 14:30 bis: 14:45". Matches iOS `description`. */
    override val description: String
        get() {
            val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault())
            val startStr = timeFormatter.format(start)
            val endStr = end?.let { timeFormatter.format(it) } ?: ""
            return "Von: $startStr bis: $endStr"
        }
}

// --- Extension functions (matching iOS extension on BreastFeeding) ---

/** Duration in seconds. Matches iOS `getDurationInSeconds()`. */
fun BreastFeeding.getDurationInSeconds(): Long {
    val endTime = end ?: Instant.now()
    return Duration.between(start, endTime).seconds
}

/**
 * Time since this feeding ended (or now if still active).
 * Matches iOS `displayTimeSinceLastFeeding()`.
 */
fun BreastFeeding.displayTimeSinceLastFeeding(): String {
    val reference = when {
        end != null -> end
        active -> Instant.now()
        else -> start
    }
    val duration = Duration.between(reference, Instant.now())
    val hours = duration.toHours()
    val minutes = (duration.toMinutes() % 60).toInt()
    val postfix = if (hours > 0) " Stunden" else "Minuten"
    return String.format("%02d:%02d %s", hours, minutes, postfix)
}
