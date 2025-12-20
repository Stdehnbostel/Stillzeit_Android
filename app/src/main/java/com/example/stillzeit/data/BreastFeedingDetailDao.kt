package com.example.stillzeit.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BreastFeedingDetailDao {
    @Query("SELECT * FROM breastFeeding")
    fun getAll(): List<BreastFeedingDetail>

    @Query("SELECT * FROM breastFeeding WHERE uid IN (:breastFeedingIds)")
    fun loadAllByIds(breastFeedingIds: IntArray): List<BreastFeedingDetail>

    @Insert
    fun insertAll(vararg breastFeedings: BreastFeedingDetail)

    @Delete
    fun delete(breastFeeding: BreastFeedingDetail)
}