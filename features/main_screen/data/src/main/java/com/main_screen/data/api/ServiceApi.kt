package com.main_screen.data.api

import com.main_screen.data.network_model.FullFilmInfo
import com.main_screen.data.network_model.NetworkModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {
    @GET("/api/v2.2/films/top")
    suspend fun getTopFilms(
        @Query("type") type: String
    ): NetworkModel

    @GET("/api/v2.2/films/{id}")
    suspend fun getTopFilms(
        @Path("id") filmId: Int
    ): FullFilmInfo
}