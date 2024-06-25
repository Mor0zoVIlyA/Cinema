package com.main_screen.presentation.view_models

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.main_screen.domain.FilmCard
import com.main_screen.domain.use_cases.FetchFilmsUseCase
import com.main_screen.domain.Result
import com.main_screen.domain.use_cases.DeleteUseCase
import com.main_screen.domain.use_cases.GetFavoritesIdUseCase
import com.main_screen.domain.use_cases.NetworkMonitorUseCase
import com.main_screen.domain.use_cases.SaveToDataBaseUseCase
import com.main_screen.presentation.R
import com.main_screen.presentation.us_state.UiItem
import com.main_screen.presentation.us_state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteMainViewModel @Inject constructor(
    private val fetchFilmsUseCase: FetchFilmsUseCase,
    private val networkMonitorUseCase: NetworkMonitorUseCase,
    private val saveToDataBaseUseCase: SaveToDataBaseUseCase,
    private val getFavoritesIdUseCase: GetFavoritesIdUseCase,
    deleteUseCase: DeleteUseCase
) : BaseViewModel(deleteUseCase) {
    private val uiState = MutableStateFlow(
        UiState(R.string.top_from_internet, emptyList())
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {

            networkMonitorUseCase.isNetworkAvailable()
                .combine(getFavoritesIdUseCase.getFavoriteFilmsId()) { internetAvailable, favorites ->
                    val filmList = uiState.value.filmList.map { it.filmCard }
                    tryDownload(internetAvailable, filmList, favorites)
                    mergePreviousResult(favorites, uiState.value.filmList)
                    checkLoadedData(internetAvailable, filmList)
                }
                .flowOn(Dispatchers.IO)
                .collect {

                }

//            networkMonitorUseCase.isNetworkAvailable()
//                .combine(getFavoritesIdUseCase.getFavoriteFilmsId()).collect { internetAvailable ->

//            }
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
        Log.d("RemoteMainViewModel", "itemClick: navigate to${filmCard.filmId}")
        navController.navigate("remoteDetails/${filmCard.filmId}")
    }

    private suspend fun tryDownload(
        internetAvailable: Boolean,
        filmList: List<FilmCard>,
        favoritesList: List<Int>
    ) {
        if (internetAvailable && filmList.isEmpty()) {
            val fetchResult = fetchFilmsUseCase.fetchFilms()
            if (fetchResult is Result.Success) {
                val resultList = convertToUiModel(fetchResult.data, favoritesList)
                uiState.value = uiState.value.copy(filmList = resultList)
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

    private fun convertToUiModel(cardList: List<FilmCard>, favoritesList: List<Int>): List<UiItem> {
        return cardList.map { film ->
            UiItem(film, favoritesList.contains(film.filmId))
        }
    }
    private fun mergePreviousResult(favoritesList: List<Int>, uiItemList: List<UiItem>) {
        uiState.value = uiState.value.copy(
           filmList =  uiItemList.map { UiItem(it.filmCard, favoritesList.contains(it.filmCard.filmId)) }
        )
    }
}