package com.stillzeit.android.domain.model

import com.stillzeit.android.model.enums.BottleType
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Domain model combining Feeding + BottleFeedingDetail (flat structure).
 * Corresponds to iOS BottleFeeding struct.
 */
data class BottleFeeding(
    val feedingId: Long? = null,
    val feedingUuid: String,
    override val volume: Long? = null,
    override val mealId: String,
    override val start: Instant,
    val bottleType: BottleType
) : MealComponent {

    override val id: String get() = feedingUuid

    /**
     * Adds a default 10 minutes for display purposes (bottle feeding duration is not tracked).
     * Matches iOS behavior.
     */
    override val timeInterval: Pair<Instant, Instant?>
        get() {
            val end = start.plusSeconds(10 * 60)
            return start to end
        }

    /** e.g. "120 ml". Matches iOS `title`. */
    override val title: String
        get() = "${volume ?: 0} ml"

    /** e.g. "14:30 Uhr, 120 ml". Matches iOS `description`. */
    override val description: String
        get() {
            val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault())
            return "${timeFormatter.format(start)} Uhr, ${volume ?: 0} ml"
        }
}
