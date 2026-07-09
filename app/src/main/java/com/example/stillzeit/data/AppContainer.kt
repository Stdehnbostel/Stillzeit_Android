package com.example.stillzeit.data

import android.content.Context
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

interface AppContainer {
    val mealsRepository : MealsRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val database by lazy {
        AppDatabase.getDatabase(context)
    }
    private val mealDao by lazy {
        database.mealDao()
    }

    private val breastFeedingDao by lazy {
        database.breastFeedingDao()
    }

    private val bottleFeedingDao by lazy {
        database.bottleFeedingDao()
    }

    private val otherFeedingDao by lazy {
        database.otherFeedingDao()
    }

    private val pumpingDao by lazy {
        database.pumpingDao()
    }

    override val mealsRepository: MealsRepository by lazy {
        LocalMealsRepository(mealDao, breastFeedingDao, bottleFeedingDao, otherFeedingDao, pumpingDao)
    }
}