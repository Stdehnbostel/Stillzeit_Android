package com.example.stillzeit.data

import androidx.room.Embedded
import androidx.room.Relation
import java.util.UUID

data class OtherFeeding(
    @Embedded
    val feeding: FeedingEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "feedingId"
    )
    val detail: OtherFeedingDetailEntity
) : MealComponent {
    override val description: String
        get() = TODO("Not yet implemented")

    override val id: String?
        get() = feeding.id

    override val mealId: UUID
        get() = feeding.mealId

    override val intervallTime: Pair<String, String?>
        get() = TODO("Not yet implemented")

    override val start: String
        get() = detail.start

    override val title: String
        get() = TODO("Not yet implemented")

    override val volume: Int
        get() = feeding.volume ?: 0

    override fun compareTo(other: MealComponent): Int {
        TODO("Not yet implemented")
    }
}
