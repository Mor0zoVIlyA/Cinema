package com.main_screen.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.main_screen.domain.FilmCard
import com.main_screen.domain.use_cases.DeleteUseCase
import com.main_screen.presentation.us_state.UiState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val deleteUseCase: DeleteUseCase
): ViewModel() {
    abstract fun getUiState(): StateFlow<UiState>
    abstract fun errorMessageState(): SharedFlow<String>
    abstract fun longClick(filmCard: FilmCard)
    abstract fun itemClick(filmCard: FilmCard, navController: NavController)
    fun delete(filmCard: FilmCard, isFavorite: Boolean){
        if (isFavorite){
            viewModelScope.launch {
                deleteUseCase.deleteItem(filmCard)
            }
        }
    }
}