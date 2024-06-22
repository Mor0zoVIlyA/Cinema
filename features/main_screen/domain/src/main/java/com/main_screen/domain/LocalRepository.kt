package com.main_screen.domain

import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    fun getFilms(): Flow<List<FilmCard>>
    suspend fun saveFilm(filmCard: FilmCard)
    suspend fun deleteFilm(filmCard: FilmCard)
}