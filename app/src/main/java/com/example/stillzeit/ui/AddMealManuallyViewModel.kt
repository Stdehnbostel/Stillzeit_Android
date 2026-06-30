package com.example.stillzeit.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stillzeit.StillzeitApplication
import com.example.stillzeit.data.MealsRepository

enum class MealTypePage {
    MENU,
    BREAST,
}

class AddMealManuallyViewModel(val repository: MealsRepository) : ViewModel() {
    var page by mutableStateOf(MealTypePage.MENU)
        private set

    fun setAddMealPage(page: MealTypePage) {
        Log.i("MealTypePage", "setAddMealPage: $page")
        this.page = page
    }

    fun getAddMealPage(): MealTypePage {
        return page
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as StillzeitApplication)
                val mealsRepository = application.container.mealsRepository
                AddMealManuallyViewModel(repository = mealsRepository)
            }
        }
    }
}
