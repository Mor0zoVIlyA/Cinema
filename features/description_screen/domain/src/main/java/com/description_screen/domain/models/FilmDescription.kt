package com.description_screen.domain.models

data class FilmDescription (
    val id: String,
    val year: String,
    val name: String,
    val url: String,
    val genre: String,
    val description: String,
    val country: String
)
val EMPTY_DESCRIPTION = FilmDescription(
    "", "", "", "","", "", "")