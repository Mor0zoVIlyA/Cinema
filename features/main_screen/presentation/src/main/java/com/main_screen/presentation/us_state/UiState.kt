package com.main_screen.presentation.us_state

import androidx.annotation.StringRes

data class UiState(
    @StringRes val label: Int,
    val filmList: List<UiItem>,
    val internetAbility:Boolean = true
)