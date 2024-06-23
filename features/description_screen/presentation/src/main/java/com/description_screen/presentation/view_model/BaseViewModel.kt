package com.description_screen.presentation.view_model

import androidx.compose.ui.platform.DisableContentCapture
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.description_screen.domain.models.EMPTY_DESCRIPTION
import com.description_screen.domain.models.FilmDescription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel(
) : ViewModel() {
    private val descriptionState = MutableStateFlow(
        EMPTY_DESCRIPTION
    )
    init{
        viewModelScope.launch(Dispatchers.IO) {
            descriptionState.value = fetchDescription()
        }
    }
    fun getUiState(): StateFlow<FilmDescription>{
        return descriptionState.asStateFlow()
    }
    abstract suspend fun fetchDescription(): FilmDescription
}