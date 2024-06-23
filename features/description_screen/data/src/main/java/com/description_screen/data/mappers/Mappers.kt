package com.description_screen.data.mappers

import com.description_screen.data.network.network_model.FullFilmInfo
import com.description_screen.domain.models.FilmDescription
import com.example.database.entities.FilmWithDescription

fun FullFilmInfo.toFilmDescription() = FilmDescription(
    kinopoiskId.toString(),
    year.toString(),
    nameRu,
    posterUrl,
    genres.joinToString { it.genre },
    description,
    countries.joinToString { it.country }
)

fun FilmWithDescription.toFilmDescription() = FilmDescription(
    film.id.toString(),
    film.year,
    film.name,
    film.path,
    description.genre,
    description.description,
    description.country
)