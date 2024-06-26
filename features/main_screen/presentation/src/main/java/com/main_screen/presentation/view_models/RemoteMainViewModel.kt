package com.main_screen.presentation.view_models

import android.util.Log
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.main_screen.domain.FilmCard
import com.main_screen.domain.use_cases.FetchFilmsUseCase
import com.main_screen.domain.Result
import com.main_screen.domain.processResult
import com.main_screen.domain.states.DownloadProgress
import com.main_screen.domain.use_cases.DeleteUseCase
import com.main_screen.domain.use_cases.GetFavoritesIdUseCase
import com.main_screen.domain.use_cases.NetworkMonitorUseCase
import com.main_screen.domain.use_cases.SaveToDataBaseUseCase
import com.main_screen.presentation.R
import com.main_screen.presentation.us_state.UiItem
import com.main_screen.presentation.us_state.UiResult
import com.main_screen.presentation.us_state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
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
    private val searchFlow = MutableStateFlow("")
    private val errorSingleMessage = MutableSharedFlow<String>()

    init {
        viewModelScope.launch {
            networkMonitorUseCase.isNetworkAvailable().flatMapLatest { internetAvailable ->
                flow {
                    val filmList = uiState.value.filmList.map { it.filmCard }
                    emit(
                        Pair(
                            internetAvailable,
                            tryDownload(internetAvailable, filmList)
                        )
                    )
                }
            }.combine(getFavoritesIdUseCase.getFavoriteFilmsId()) { result, idList ->
                when (result.second) {
                    is UiResult.Success -> {
                        val success = (result.second as UiResult.Success<List<FilmCard>>).data
                        val uiModelList = convertToUiModel(success, idList)
                        val mergedResult = mergePreviousResult(idList, uiModelList)
                        UiResult.Success(mergedResult, result.first)
                    }
                    is UiResult.Error -> {
                        val error = (result.second as UiResult.Error<List<FilmCard>>).error
                        UiResult.Error<List<UiItem>>(error, result.first)
                    }
                    else -> UiResult.Loaded<List<UiItem>>(result.first)
                }

            }
                .collect { result ->
                    when (result) {
                        is UiResult.Success -> {
                            uiState.value = uiState.value.copy(
                                filmList = result.data,
                            )
                        }
                        is UiResult.Error -> {
                            errorSingleMessage.tryEmit(result.error.message?: "unknown error occurred")
                        }
                        is UiResult.Loaded -> {

                        }
                    }
                    uiState.value = uiState.value.copy(
                        internetAbility = isAllowToShow(result.internetAbility, uiState.value.filmList),
                    )
                }
        }
    }

    override fun getUiState(): StateFlow<UiState> {
        return uiState.asStateFlow()
    }

    override fun errorMessageState(): SharedFlow<String> {
        return errorSingleMessage.asSharedFlow()
    }

    override fun longClick(filmCard: FilmCard) {
        viewModelScope.launch(Dispatchers.IO) {
            saveToDataBaseUseCase.saveFilm(filmCard).collect { downloadProgress ->
                val previousList = uiState.value.filmList

                when (downloadProgress) {
                    is DownloadProgress.Success -> {
                        val uiItemList = processList(filmCard, previousList) { uiItem ->
                            uiItem.copy(isLoading = false)
                        }
                        uiState.value = uiState.value.copy(filmList = uiItemList)
                    }

                    is DownloadProgress.Error -> {}

                    is DownloadProgress.Progress -> {
                        val uiItemList = processList(filmCard, previousList) { uiItem ->
                            uiItem.copy(
                                loadingProgress = downloadProgress.progress,
                                isLoading = true
                            )
                        }
                        uiState.value = uiState.value.copy(filmList = uiItemList)
                    }
                }
            }
        }
    }

    private suspend fun processList(
        filmCard: FilmCard,
        previousList: List<UiItem>,
        mapCallback: (UiItem) -> UiItem
    ): List<UiItem> = withContext(Dispatchers.IO) {
        return@withContext previousList.map { uiItem ->
            if (uiItem.filmCard == filmCard) {
                mapCallback(uiItem)
            } else
                uiItem
        }
    }

    override fun itemClick(filmCard: FilmCard, navController: NavController) {
        Log.d("RemoteMainViewModel", "itemClick: navigate to${filmCard.filmId}")
        navController.navigate("remoteDetails/${filmCard.filmId}")
    }

    private suspend fun tryDownload(
        internetAvailable: Boolean,
        filmList: List<FilmCard>,
    ): UiResult<List<FilmCard>> {
        if (filmList.isNotEmpty()) {
            return UiResult.Success(filmList, internetAvailable)
        }
        if (internetAvailable) {
            return fetchFilmsUseCase.fetchFilms().processResult(
                { success -> UiResult.Success(success.data, internetAvailable) },
                { error -> UiResult.Error(error.error, internetAvailable) },
            )
        }
        return UiResult.Loaded(internetAvailable)
    }

    private fun isAllowToShow(internetAvailable: Boolean, filmList: List<UiItem>): Boolean {
        return internetAvailable || filmList.isNotEmpty()
    }

    private fun convertToUiModel(cardList: List<FilmCard>, favoritesList: List<Int>): List<UiItem> {
        return cardList.map { film ->
            UiItem(film, favoritesList.contains(film.filmId), false)
        }
    }

    private fun mergePreviousResult(
        favoritesList: List<Int>,
        uiItemList: List<UiItem>
    ): List<UiItem> {
        return uiItemList.map {
            UiItem(
                it.filmCard,
                favoritesList.contains(it.filmCard.filmId),
                it.isLoading
            )
        }
    }
}