package com.main_screen.domain.use_cases

import com.main_screen.domain.LocalRepository
import javax.inject.Inject

class GetListFromDataBase @Inject constructor(
    private val localRepository: LocalRepository
) {
    fun getList() = localRepository.getFilms()
}