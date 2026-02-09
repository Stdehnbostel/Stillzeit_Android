package com.stillzeit.android.domain.model

import java.time.Instant

/**
 * Sealed interface corresponding to the iOS MealComponent protocol.
 * All feeding types implement this interface for polymorphic handling.
 */
sealed interface MealComponent : Comparable<MealComponent> {
    val id: String?
    val timeInterval: Pair<Instant, Instant?>
    val title: String
    val description: String
    val start: Instant
    val volume: Long?
    val mealId: String

    override fun compareTo(other: MealComponent): Int =
        start.compareTo(other.start)
}
