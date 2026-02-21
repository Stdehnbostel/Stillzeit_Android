package com.example.stillzeit.data

interface AppContainer {
    val mealsRepository : MealsRepository
}

class DefaultAppContainer : AppContainer {

    override val mealsRepository: MealsRepository by lazy {
        LocalMealsRepository()
    }
}