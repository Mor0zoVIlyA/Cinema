package com.main_screen.presentation.view_models

import androidx.lifecycle.ViewModel
import com.main_screen.domain.FilmCard
import com.main_screen.presentation.us_state.UiState
import kotlinx.coroutines.flow.StateFlow

class LocalMainViewModel: BaseViewModel() {
    override fun getUiState(): StateFlow<UiState> {
        TODO("Not yet implemented")
    }

    override fun getType(): ViewModelType {
        TODO("Not yet implemented")
    }

    override fun longClick(filmCard: FilmCard) {
        TODO("Not yet implemented")
    }

    override fun itemClick(filmCard: FilmCard) {
        TODO("Not yet implemented")
    }
}