package com.main_screen.domain.use_cases

import com.main_screen.domain.RemoteRepository
import javax.inject.Inject

class FetchFilmsUseCase @Inject constructor(
    private val repository: RemoteRepository
) {
    suspend fun fetchFilms() = repository.fetchFilms("TOP_100_POPULAR_FILMS")
}