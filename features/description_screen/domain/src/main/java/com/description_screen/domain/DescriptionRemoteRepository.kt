package com.description_screen.domain

import com.description_screen.domain.models.FilmDescription

interface DescriptionRemoteRepository {
    suspend fun fetchInfo(id: Int): FilmDescription
}