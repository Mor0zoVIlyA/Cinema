package com.main_screen.data

import com.main_screen.domain.FilmCard

sealed class State {
    class Success(resultList: List<FilmCard>): State()
    class Error(errorMessage: String): State()
}