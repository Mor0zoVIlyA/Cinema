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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteMainViewModel @Inject constructor(
    private val fetchFilmsUseCase: FetchFilmsUseCase,
    private val networkMonitorUseCase: NetworkMonitorUseCase,
    private val saveToDataBaseUseCase: SaveToDataBaseUseCase
) : BaseViewModel() {
    private val uiState = MutableStateFlow(
        UiState(R.string.top_from_internet, emptyList())
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            networkMonitorUseCase.isNetworkAvailable().collect { internetAvailable ->
                val filmList = uiState.value.filmList
                tryDownload(internetAvailable, filmList)
                checkLoadedData(internetAvailable, filmList)
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

    private suspend fun tryDownload(internetAvailable: Boolean, filmList: List<FilmCard>) {
        if (internetAvailable && filmList.isEmpty()) {
            val fetchResult = fetchFilmsUseCase.fetchFilms()
            if (fetchResult is Result.Success) {
                uiState.value = uiState.value.copy(filmList = fetchResult.data)
            }
        }
    }

    private fun checkLoadedData(internetAvailable: Boolean, filmList: List<FilmCard>) {
        if (!internetAvailable) {
            if (filmList.isEmpty()) {
                uiState.value = uiState.value.copy(internetAbility = false)
            } else {
                uiState.value = uiState.value.copy(internetAbility = true)
            }
        } else {
            uiState.value = uiState.value.copy(internetAbility = true)
        }
    }
}