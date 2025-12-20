package com.example.stillzeit.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.stillzeit.data.BreastFeeding

@Dao
interface BreastFeedingDao {
    @Query("SELECT * FROM breastFeeding")
    fun getAll(): List<BreastFeeding>

    @Query("SELECT * FROM breastFeeding WHERE uid IN (:breastFeedingIds)")
    fun loadAllByIds(breastFeedingIds: IntArray): List<BreastFeeding>

    @Insert
    fun insertAll(vararg breastFeedings: BreastFeeding)

    @Delete
    fun delete(breastFeeding: BreastFeeding)
}