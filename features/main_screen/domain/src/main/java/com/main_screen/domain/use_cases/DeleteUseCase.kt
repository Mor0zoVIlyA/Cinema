package com.main_screen.domain.use_cases

import com.main_screen.domain.FilmCard
import com.main_screen.domain.LocalRepository
import javax.inject.Inject

class DeleteUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend fun deleteItem(card: FilmCard) = localRepository.deleteFilm(card)
}