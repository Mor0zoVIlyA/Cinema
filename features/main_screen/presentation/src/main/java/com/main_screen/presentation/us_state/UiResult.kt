package com.main_screen.presentation.us_state

sealed class UiResult<T>(val internetAbility: Boolean) {
    data class Success<T>(val data: T, val ability: Boolean) : UiResult<T>(ability)
    data class Error<T>(val error: Throwable, val ability: Boolean) : UiResult<T>(ability)
    data class Loaded<T>(val ability: Boolean) : UiResult<T>(ability)
}