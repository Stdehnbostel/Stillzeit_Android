package com.example.stillzeit.data

import androidx.room.Embedded
import androidx.room.Relation
import java.util.UUID

enum class BottleType(val type: String) {
    MILK("milk"),
    FORMULA("formula"),
}

data class BottleFeeding(
    @Embedded
    val feeding: FeedingEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "feedingId"
    )
     val detail: BottleFeedingDetailEntity
) : MealComponent {
    override val description: String
        get() = TODO("Not yet implemented")

    override val id: String
        get() = feeding.id

    override fun compareTo(other: MealComponent): Int {
        TODO("Not yet implemented")
    }

    override val intervallTime: Pair<String, String?>
        get() = TODO("Not yet implemented")

    override val mealId: UUID
        get() = feeding.mealId

    override val start: String
        get() = detail.start

    override val title: String
        get() = TODO("Not yet implemented")

    override val volume: Int
        get() = feeding.volume ?: 0
}
