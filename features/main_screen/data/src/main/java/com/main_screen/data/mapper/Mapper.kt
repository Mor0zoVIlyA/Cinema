package com.main_screen.data.mapper

import com.main_screen.data.network_model.Film
import com.main_screen.domain.FilmCard

fun Film.toFilmCard() = FilmCard(
    filmId, countries.joinToString { it.country } , nameRu, year, posterUrl
)