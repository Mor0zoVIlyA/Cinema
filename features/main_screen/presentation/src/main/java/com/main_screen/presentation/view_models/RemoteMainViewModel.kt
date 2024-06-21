package com.main_screen.presentation.view_models

import androidx.lifecycle.viewModelScope
import com.main_screen.domain.FilmCard
import com.main_screen.domain.use_cases.FetchFilmsUseCase
import com.main_screen.domain.Result
import com.main_screen.domain.use_cases.NetworkMonitorUseCase
import com.main_screen.presentation.R
import com.main_screen.presentation.us_state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteMainViewModel @Inject constructor(
    private val fetchFilmsUseCase: FetchFilmsUseCase,
    private val networkMonitorUseCase: NetworkMonitorUseCase
) : BaseViewModel() {
    private val filmListFlow = MutableStateFlow<List<FilmCard>>(emptyList())
    private val uiState = MutableStateFlow(
        UiState(R.string.top_from_internet, emptyList())
    )

    init {
        viewModelScope.launch {
            val result = fetchFilmsUseCase.fetchFilms()
            if (result is Result.Success){
                uiState.value = uiState.value.copy(filmList = result.data)
            }
        }
//        combine(
//            filmListFlow
//        ) { filmList, internetAvailable ->
//            if (internetAvailable && filmList.isEmpty()) {
//                val fetchResult = fetchFilmsUseCase.fetchFilms()
//                if (fetchResult is Result.Success) {
//                    filmListFlow.value = fetchResult.data
//                }
//            }
//            if (!internetAvailable && filmList.isEmpty()) {
//                uiState.value = uiState.value.copy(internetAbility = false)
//            }
//
//        }

    }

    fun getFilmList(): StateFlow<List<FilmCard>> = filmListFlow.asStateFlow()
    override fun getUiState(): StateFlow<UiState> {
        return uiState.asStateFlow()
    }

    override fun getType(): ViewModelType {
        return ViewModelType.REMOTE
    }
    override fun longClick(filmCard: FilmCard) {
        TODO("Not yet implemented")
    }

    override fun itemClick(filmCard: FilmCard) {
        TODO("Not yet implemented")
    }
}