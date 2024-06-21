package com.main_screen.domain

import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
    suspend fun fetchFilms(filmRequestType: String): Result<List<FilmCard>>
    fun internetConnectionFlow(): Flow<Boolean>
}