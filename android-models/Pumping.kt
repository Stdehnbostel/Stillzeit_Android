package com.stillzeit.android.domain.model

import com.stillzeit.android.model.enums.BreastFeedingSide
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Domain model combining Feeding + PumpingDetail (flat structure).
 * Corresponds to iOS Pumping struct.
 * Note: `side` is typed as BreastFeedingSide (instead of raw String as in iOS) for consistency.
 */
data class Pumping(
    val feedingId: Long? = null,
    val feedingUuid: String,
    override val volume: Long? = null,
    override val mealId: String,
    override val start: Instant,
    val side: BreastFeedingSide
) : MealComponent {

    override val id: String get() = feedingUuid

    /**
     * Adds a default 10 minutes for display purposes (pumping duration is not tracked).
     * Matches iOS behavior.
     */
    override val timeInterval: Pair<Instant, Instant?>
        get() {
            val end = start.plusSeconds(10 * 60)
            return start to end
        }

    /** e.g. "80 ml". Matches iOS `title`. */
    override val title: String
        get() = "${volume ?: 0} ml"

    /** Formatted start time. Matches iOS `description`. */
    override val description: String
        get() {
            val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault())
            return timeFormatter.format(start)
        }
}
