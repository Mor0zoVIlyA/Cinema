package com.main_screen.data

import android.util.Log
import com.example.database.entities.FilmEntityDao
import com.main_screen.data.api.ServiceApi
import com.main_screen.data.mapper.toDataBaseModel
import com.main_screen.data.mapper.toFilmCard
import com.main_screen.data.utils.ImageDownloader
import com.main_screen.domain.FilmCard
import com.main_screen.domain.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.CancellationException
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val dao: FilmEntityDao,
    private val serviceApi: ServiceApi,
    private val file: File
): LocalRepository {
    override fun getFilms(): Flow<List<FilmCard>> {
       return dao.getAllFilms().map { list ->
            list.map { entity ->
                entity.toFilmCard()
            }
       }
    }

    override suspend fun saveFilm(filmCard: FilmCard) {
        try {
            val description = serviceApi.getTopFilms(filmId = filmCard.filmId)
            val picturePath = ImageDownloader().downloadImage(file, filmCard.posterUrl)
            if (picturePath.isNotEmpty())
                dao.insertFilm(description.toDataBaseModel(filmCard, picturePath))
        }catch (e:Exception){
            if (e is CancellationException)
                throw e
            Log.d("TAG", "saveFilm: $e")
        }

    }

    override suspend fun deleteFilm(filmCard: FilmCard) {
        TODO("Not yet implemented")
    }
}