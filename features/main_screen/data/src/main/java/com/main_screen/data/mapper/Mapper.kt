package com.main_screen.data.mapper

import com.example.database.entities.FilmDB
import com.main_screen.data.network_model.Film
import com.main_screen.domain.FilmCard

fun Film.toFilmCard() = FilmCard(
    filmId, year, nameRu, posterUrl
)

fun FilmDB.toFilmCard() = FilmCard(
    id, year, name, path
)