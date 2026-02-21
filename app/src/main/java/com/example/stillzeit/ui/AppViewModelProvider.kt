package com.example.stillzeit.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stillzeit.StillzeitApplication
import com.example.stillzeit.data.MealsRepository

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AddMealViewModel(
                mealsRepository()
            )
        }
        initializer {
            ProtocolViewModel(
                mealsRepository()
            )
        }
    }
}

fun CreationExtras.stillzeitApplication(): StillzeitApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StillzeitApplication)

fun CreationExtras.mealsRepository(): MealsRepository =
    stillzeitApplication().container.mealsRepository
