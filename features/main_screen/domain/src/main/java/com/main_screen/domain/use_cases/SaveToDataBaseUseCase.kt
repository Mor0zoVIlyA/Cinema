package com.main_screen.domain.use_cases

import com.main_screen.domain.FilmCard
import com.main_screen.domain.LocalRepository
import javax.inject.Inject

class SaveToDataBaseUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    fun saveFilm(filmCard: FilmCard) = localRepository.saveFilmWithProgress(filmCard)
}