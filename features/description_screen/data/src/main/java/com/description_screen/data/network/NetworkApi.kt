package com.description_screen.data.network

import com.description_screen.data.network.network_model.FullFilmInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {
    @GET("/api/v2.2/films/{id}")
    suspend fun getFullInfo(
        @Path("id") filmId: Int
    ): FullFilmInfo
}