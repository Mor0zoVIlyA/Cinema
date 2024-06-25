package com.description_screen.data.mappers

import com.description_screen.data.network.network_model.FullFilmInfo
import com.description_screen.domain.models.FilmDescription
import com.example.database.entities.FilmWithDescription

fun FullFilmInfo.toFilmDescription() = FilmDescription(
    kinopoiskId.toString(),
    year.toString()?: "unable to receive data",
    nameRu ?: "unable to receive data",
    posterUrl ?: "unable to receive data",
    genres.joinToString { it.genre } ?: "unable to receive data",
    description ?: "unable to receive data",
    countries.joinToString { it.country } ?: "unable to receive data"
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