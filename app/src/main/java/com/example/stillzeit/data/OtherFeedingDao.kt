package com.example.stillzeit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import java.util.UUID

@Dao
interface OtherFeedingDao {
    @Insert
    suspend fun insertFeeding(feeding: FeedingEntity)

    @Insert
    suspend fun insertDetail(detail: OtherFeedingDetailEntity)

    @Transaction
    suspend fun insert(otherFeeding: OtherFeeding) {
        insertFeeding(otherFeeding.feeding)
        insertDetail(otherFeeding.detail)
    }

    @Transaction
    @Query("SELECT * from feeding WHERE mealId = :mealId AND type = 'OTHER'")
    suspend fun getFeedingsForMeal(mealId: UUID): List<OtherFeeding>
}