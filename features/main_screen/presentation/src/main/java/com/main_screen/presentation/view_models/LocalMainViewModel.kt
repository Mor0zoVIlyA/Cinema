package com.main_screen.presentation.view_models

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.main_screen.domain.FilmCard
import com.main_screen.domain.use_cases.DeleteUseCase
import com.main_screen.domain.use_cases.GetListFromDataBase
import com.main_screen.presentation.R
import com.main_screen.presentation.us_state.UiItem
import com.main_screen.presentation.us_state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalMainViewModel @Inject constructor(
    private val getListFromDataBase: GetListFromDataBase,
    deleteUseCase: DeleteUseCase
): BaseViewModel(deleteUseCase) {
    private val uiState = MutableStateFlow(UiState(R.string.favorites, emptyList()))
    init {
        viewModelScope.launch {
            getListFromDataBase.getList().collect{ filmList ->
                uiState.value = uiState.value.copy(filmList = filmList.map { UiItem(it, true, false) })
            }
        }
    }
    override fun getUiState(): StateFlow<UiState> {
        return uiState.asStateFlow()
    }

    override fun errorMessageState(): SharedFlow<String> {
        return MutableSharedFlow<String>().asSharedFlow()
    }

    override fun longClick(filmCard: FilmCard) {
    }

    override fun itemClick(filmCard: FilmCard, navController: NavController) {
        navController.navigate("localDetails/${filmCard.filmId}")
    }
}