package com.stillzeit.android.domain.model

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Domain model combining Feeding + OtherFeedingDetail (flat structure).
 * Corresponds to iOS OtherFeeding struct.
 */
data class OtherFeeding(
    val feedingId: Long? = null,
    val feedingUuid: String,
    override val volume: Long? = null,
    override val mealId: String,
    override val start: Instant,
    val feedingDescription: String? = null
) : MealComponent {

    override val id: String get() = feedingUuid

    /**
     * Adds a default 10 minutes for display purposes (other feeding duration is not tracked).
     * Matches iOS behavior.
     */
    override val timeInterval: Pair<Instant, Instant?>
        get() {
            val end = start.plusSeconds(10 * 60)
            return start to end
        }

    /** e.g. "50 g". Matches iOS `title`. */
    override val title: String
        get() = "${volume ?: 0} g"

    /** e.g. "14:30 Uhr, 50 g\nBrei". Matches iOS `description`. */
    override val description: String
        get() {
            val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault())
            val desc = feedingDescription?.let { "\n$it" } ?: ""
            return "${timeFormatter.format(start)} Uhr, ${volume ?: 0} g$desc"
        }
}
