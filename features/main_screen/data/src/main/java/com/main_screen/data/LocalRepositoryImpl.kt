package com.main_screen.data

import android.util.Log
import com.example.database.entities.FilmEntityDao
import com.main_screen.data.api.ServiceApi
import com.main_screen.data.mapper.toDataBaseModel
import com.main_screen.data.mapper.toFilmCard
import com.main_screen.data.mapper.toFilmDb
import com.main_screen.data.utils.DownloadState
import com.main_screen.data.utils.ImageDownloader
import com.main_screen.domain.FilmCard
import com.main_screen.domain.LocalRepository
import com.main_screen.domain.states.DownloadProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.util.concurrent.CancellationException
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val dao: FilmEntityDao,
    private val serviceApi: ServiceApi,
    private val file: File
) : LocalRepository {
    override fun getFilms(): Flow<List<FilmCard>> {
        return dao.getAllFilms().map { list ->
            list.map { entity ->
                entity.toFilmCard()
            }
        }
    }

    override fun saveFilmWithProgress(filmCard: FilmCard): Flow<DownloadProgress> = flow {
        try {
            val description = serviceApi.getTopFilms(filmId = filmCard.filmId)
            ImageDownloader().downloadImage(file, filmCard.posterUrl).collect { state ->
                when (state) {
                    is DownloadState.Progress -> {
                        val progress = DownloadProgress.Progress(state.percentage)
                        emit(progress)
                    }
                    is DownloadState.Error -> {
                        emit(DownloadProgress.Error(state.exception.message ?: "unable get message"))
                    }
                    is DownloadState.Success -> {
                        try {
                            dao.insertFilm(description.toDataBaseModel(filmCard, state.filePath))
                            emit(DownloadProgress.Success)
                        } catch (e: Exception) {
                            emit(DownloadProgress.Error(e.message ?: "unable get message"))
                            if (e is CancellationException)
                                throw e
                            Log.d("TAG", "saveFilm: $e")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            if (e is CancellationException)
                throw e
            Log.d("TAG", "saveFilm: $e")
        }

    }

    override suspend fun deleteFilm(filmCard: FilmCard) {
        try {
            dao.deleteFilm(filmCard.toFilmDb(filmCard.posterUrl))
        } catch (e: Exception) {
            if (e is CancellationException)
                throw e
            Log.d("TAG", "deleteFilm: $e")
        }
    }

    override fun getFavoriteFilmsId(): Flow<List<Int>> {
        return dao.favoriteId()
    }
}