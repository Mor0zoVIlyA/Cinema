package com.description_screen.data

import com.description_screen.data.mappers.toFilmDescription
import com.description_screen.data.network.NetworkApi
import com.description_screen.domain.DescriptionRemoteRepository
import com.description_screen.domain.models.EMPTY_DESCRIPTION
import com.description_screen.domain.models.FilmDescription
import java.util.concurrent.CancellationException
import javax.inject.Inject

class DescriptionRemoteRepositoryImpl @Inject constructor(
    private val networkApi: NetworkApi
): DescriptionRemoteRepository {
    override suspend fun fetchInfo(id: Int): FilmDescription {
        try {
            return networkApi.getFullInfo(id).toFilmDescription()
        }catch (e: Exception){
            if (e is CancellationException){
                throw e
            }
        }
        return EMPTY_DESCRIPTION
    }
}