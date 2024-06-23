package com.description_screen.domain
import com.description_screen.domain.models.FilmDescription
interface DescriptionLocalRepository {
    suspend fun getInfo(id: Int): FilmDescription
}