package com.main_screen.data

import com.example.database.entities.FilmEntityDao
import com.main_screen.data.mapper.toFilmCard
import com.main_screen.domain.FilmCard
import com.main_screen.domain.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val dao: FilmEntityDao
): LocalRepository {
    override fun getFilms(): Flow<List<FilmCard>> {
       return dao.getAllFilms().map { list ->
            list.map { entity ->
                entity.toFilmCard()
            }
       }
    }

    override suspend fun saveFilm(filmCard: FilmCard) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFilm(filmCard: FilmCard) {
        TODO("Not yet implemented")
    }
}