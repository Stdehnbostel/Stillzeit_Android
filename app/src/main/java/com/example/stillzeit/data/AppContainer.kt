package com.example.stillzeit.data

import android.content.Context

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

    override val mealsRepository: MealsRepository by lazy {
        LocalMealsRepository(mealDao)
    }
}