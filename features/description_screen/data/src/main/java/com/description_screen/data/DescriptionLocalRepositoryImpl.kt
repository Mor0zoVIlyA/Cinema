package com.description_screen.data

import com.description_screen.data.mappers.toFilmDescription
import com.description_screen.domain.DescriptionLocalRepository
import com.description_screen.domain.models.EMPTY_DESCRIPTION
import com.description_screen.domain.models.FilmDescription
import com.example.database.entities.FilmEntityDao
import java.util.concurrent.CancellationException
import javax.inject.Inject

class DescriptionLocalRepositoryImpl @Inject constructor(
    private val filmEntityDao: FilmEntityDao
): DescriptionLocalRepository {
    override suspend fun getInfo(id: Int): FilmDescription {
        try {
            return filmEntityDao.getFilmDescriptionById(id).toFilmDescription()

        }catch (e: Exception){
            if (e is CancellationException){
                throw e
            }
        }
        return EMPTY_DESCRIPTION
    }
}