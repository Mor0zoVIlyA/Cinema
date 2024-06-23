package com.example.database.entities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FilmEntityDao {
    @Query("SELECT * FROM film")
    abstract fun getAllFilms(): Flow<List<FilmDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertFilm(filmDB: FilmDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertFilmDescription(description: FilmDescriptionEntity)

    @Transaction
    open suspend fun insertFilm(filmWithDescription: FilmWithDescription){
        insertFilm(filmWithDescription.film)
        insertFilmDescription(filmWithDescription.description)
    }

}