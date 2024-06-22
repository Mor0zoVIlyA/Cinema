package com.example.database.entities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmEntityDao {
    @Query("SELECT * FROM film")
    fun getAllFilms(): Flow<List<FilmDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(filmDB: FilmDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmDescription(description: FilmDescriptionEntity)

    @Transaction
    suspend fun insertFilm(filmWithDescription: FilmWithDescription){
        insertFilm(filmWithDescription.film)
        insertFilmDescription(filmWithDescription.description)
    }

}