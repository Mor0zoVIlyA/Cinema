package com.main_screen.presentation.us_state

import androidx.annotation.StringRes
import com.main_screen.domain.FilmCard

data class UiState(
    @StringRes val label: Int,
    val filmList: List<FilmCard>,
    val internetAbility:Boolean = true
)