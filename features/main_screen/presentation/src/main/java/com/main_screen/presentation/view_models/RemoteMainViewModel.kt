package com.main_screen.presentation.view_models

import android.util.Log
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.main_screen.domain.FilmCard
import com.main_screen.domain.use_cases.FetchFilmsUseCase
import com.main_screen.domain.Result
import com.main_screen.domain.use_cases.NetworkMonitorUseCase
import com.main_screen.domain.use_cases.SaveToDataBaseUseCase
import com.main_screen.presentation.R
import com.main_screen.presentation.us_state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteMainViewModel @Inject constructor(
    private val fetchFilmsUseCase: FetchFilmsUseCase,
    private val networkMonitorUseCase: NetworkMonitorUseCase,
    private val saveToDataBaseUseCase: SaveToDataBaseUseCase
) : BaseViewModel() {
    private val filmListFlow = MutableStateFlow<List<FilmCard>>(emptyList())
    private val uiState = MutableStateFlow(
        UiState(R.string.top_from_internet, emptyList())
    )

    init {
        viewModelScope.launch (Dispatchers.IO){
            combine(
                filmListFlow,
                networkMonitorUseCase.isNetworkAvailable()
            ) { filmList, internetAvailable ->
                if (internetAvailable && filmList.isEmpty()) {
                    val fetchResult = fetchFilmsUseCase.fetchFilms()
                    if (fetchResult is Result.Success) {
                        uiState.value = uiState.value.copy(filmList = fetchResult.data)
                    }
                }
                if (!internetAvailable && filmList.isEmpty()) {
                    uiState.value = uiState.value.copy(internetAbility = false)
                }

            }.collect{
            }
        }
    }

    override fun getUiState(): StateFlow<UiState> {
        return uiState.asStateFlow()
    }
    override fun longClick(filmCard: FilmCard) {
        viewModelScope.launch(Dispatchers.IO) {
            saveToDataBaseUseCase.saveFilm(filmCard)
        }
    }

    override fun itemClick(filmCard: FilmCard, navController: NavController) {
        navController.navigate("remoteDetails/${filmCard.filmId}")
    }
}