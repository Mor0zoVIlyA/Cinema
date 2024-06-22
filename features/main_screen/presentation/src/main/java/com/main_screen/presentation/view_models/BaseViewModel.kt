package com.main_screen.presentation.view_models

import androidx.lifecycle.ViewModel
import com.main_screen.domain.FilmCard
import com.main_screen.presentation.us_state.UiState
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel: ViewModel() {
    abstract fun getUiState(): StateFlow<UiState>
    abstract fun longClick(filmCard: FilmCard)
    abstract fun itemClick(filmCard: FilmCard)
}