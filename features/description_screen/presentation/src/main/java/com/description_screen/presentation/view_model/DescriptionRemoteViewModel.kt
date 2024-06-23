package com.description_screen.presentation.view_model

import androidx.navigation.NavController
import com.description_screen.domain.models.FilmDescription
import com.description_screen.domain.use_cases.GetRemoteInfoUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = DescriptionRemoteViewModel.ViewModelFactory::class)
class DescriptionRemoteViewModel @AssistedInject constructor(
    @Assisted
    private val filmId: Int,
    private val getRemoteInfoUseCase: GetRemoteInfoUseCase
): BaseViewModel() {
    override suspend fun fetchDescription(): FilmDescription {
        return getRemoteInfoUseCase.fetchRemoteInfo(filmId)
    }
    @AssistedFactory
    interface ViewModelFactory {
        fun create(id: Int): DescriptionRemoteViewModel
    }

}