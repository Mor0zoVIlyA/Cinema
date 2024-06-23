package com.description_screen.presentation.view_model

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.description_screen.domain.models.EMPTY_DESCRIPTION
import com.description_screen.domain.models.FilmDescription
import com.description_screen.domain.use_cases.GetLocalInfoUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = DescriptionLocalViewModel.ViewModelFactory::class)
class DescriptionLocalViewModel @AssistedInject constructor(
    @Assisted
    private val filmId: Int,
    private val localInfoUseCase: GetLocalInfoUseCase
): BaseViewModel() {
    override suspend fun fetchDescription(): FilmDescription {
        return localInfoUseCase.getLocalInfo(filmId)
    }

    @AssistedFactory
    interface ViewModelFactory {
        fun create(id: Int): DescriptionLocalViewModel
    }
}