package com.main_screen.presentation.us_state

import com.main_screen.domain.FilmCard

data class UiItem(
    val filmCard: FilmCard,
    val isFavorites: Boolean,
    val isLoading: Boolean,
    val loadingProgress: Float? = null
)
