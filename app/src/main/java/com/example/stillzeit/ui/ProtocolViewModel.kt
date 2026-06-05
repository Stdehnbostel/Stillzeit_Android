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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProtocolViewModel(val repository: MealsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProtocolUiState(
            getFormattedStartOfSelectedWeek(),
            getFormattedEndOfSelectedWeek(),
            emptyList()
        )
    )
    val uiState: StateFlow<ProtocolUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getMeals().collect { meals ->
                _uiState.update { currentState ->
                    currentState.copy(
                        meals = meals,
                        startOfWeek = getFormattedStartOfSelectedWeek(),
                        endOfWeek = getFormattedEndOfSelectedWeek()
                    )
                }
            }
        }
    }

    fun getFormattedEndOfSelectedWeek(): String {
        return repository.getEndOfWeek().toLocalDate().toString()
    }

    fun getFormattedStartOfSelectedWeek(): String {
        return repository.getEndOfWeek().plusDays(-7).toLocalDate().toString()
    }

    fun incrementWeek() {
        repository.incrementWeek()
        _uiState.update { currentState -> currentState.copy(
            startOfWeek = getFormattedStartOfSelectedWeek(),
            endOfWeek = getFormattedEndOfSelectedWeek()
        ) }
    }

    fun decrementWeek() {
        repository.decrementWeek()
        _uiState.update { currentState -> currentState.copy(
            startOfWeek = getFormattedStartOfSelectedWeek(),
            endOfWeek = getFormattedEndOfSelectedWeek()
        ) }
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
    val startOfWeek: String,
    val endOfWeek: String,
    val meals: List<Meal>
)
