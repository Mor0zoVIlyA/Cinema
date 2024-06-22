package com.main_screen.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main_screen.domain.FilmCard
import com.main_screen.domain.use_cases.GetListFromDataBase
import com.main_screen.presentation.R
import com.main_screen.presentation.us_state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalMainViewModel @Inject constructor(
    private val getListFromDataBase: GetListFromDataBase
): BaseViewModel() {
    private val uiState = MutableStateFlow(UiState(R.string.favorites, emptyList()))
    init {
        viewModelScope.launch {
            getListFromDataBase.getList().collect{ filmList ->
                uiState.value = uiState.value.copy(filmList = filmList)
            }
        }
    }
    override fun getUiState(): StateFlow<UiState> {
        //TODO("Not yet implemented")
        return uiState.asStateFlow()
    }

    override fun longClick(filmCard: FilmCard) {
        TODO("Not yet implemented")
    }

    override fun itemClick(filmCard: FilmCard) {
        TODO("Not yet implemented")
    }
}