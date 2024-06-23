package com.main_screen.data.mapper

import com.example.database.entities.FilmDB
import com.example.database.entities.FilmDescriptionEntity
import com.example.database.entities.FilmWithDescription
import com.main_screen.data.network_model.Film
import com.main_screen.data.network_model.FullFilmInfo
import com.main_screen.domain.FilmCard

fun Film.toFilmCard() = FilmCard(
    filmId, year, nameRu, posterUrl
)

fun FilmDB.toFilmCard() = FilmCard(
    id, year, name, path
)
fun FilmCard.toFilmDb(savedPath: String) = FilmDB(
    filmId, year, nameRu, savedPath

)
fun FullFilmInfo.toDataBaseModel(filmCard: FilmCard, path: String) = FilmWithDescription(
    filmCard.toFilmDb(path),
    FilmDescriptionEntity(
        filmCard.filmId,
        genres.joinToString { it.genre },
        description,
        countries.joinToString{it.country}
    )
)