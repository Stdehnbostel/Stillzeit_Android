package com.example.stillzeit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stillzeit.StillzeitApplication
import com.example.stillzeit.data.Meal
import com.example.stillzeit.data.MealsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.ZonedDateTime

class ProtocolViewModel(val repository: MealsRepository) : ViewModel() {
    val uiState: StateFlow<ProtocolUiState> = repository.getMeals()
        .map { meals -> ProtocolUiState(meals) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProtocolUiState(emptyList())
        )

    var endOfSelectedWeek: ZonedDateTime = if (ZonedDateTime.now().dayOfWeek == DayOfWeek.SUNDAY) {
        ZonedDateTime.now()
    } else {
        ZonedDateTime.now().plusDays(7 - ZonedDateTime.now().dayOfWeek.value.toLong())
    }

    var startOfSelectedWeek = getStartOfWeek(endOfSelectedWeek)

    fun getFormattedEndOfSelectedWeek(): String {
        return endOfSelectedWeek.toLocalDate().toString()
    }

    fun getFormattedStartOfSelectedWeek(): String {
        return startOfSelectedWeek.toLocalDate().toString()
    }


    private fun getStartOfWeek(end: ZonedDateTime): ZonedDateTime {
        val start = end.minusDays(7)
        return start
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as StillzeitApplication)
                val mealsRepository = application.container.mealsRepository
                ProtocolViewModel(repository = mealsRepository)
            }
        }
    }
}

data class ProtocolUiState(
    val meals: List<Meal>
)