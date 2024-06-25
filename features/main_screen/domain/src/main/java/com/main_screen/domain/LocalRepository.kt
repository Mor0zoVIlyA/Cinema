package com.main_screen.domain

import com.main_screen.domain.states.DownloadProgress
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    fun getFilms(): Flow<List<FilmCard>>
    fun saveFilmWithProgress(filmCard: FilmCard): Flow<DownloadProgress>
    suspend fun deleteFilm(filmCard: FilmCard)
    fun getFavoriteFilmsId(): Flow<List<Int>>
}