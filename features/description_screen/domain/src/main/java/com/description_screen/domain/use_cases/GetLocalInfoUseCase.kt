package com.description_screen.domain.use_cases

import com.description_screen.domain.DescriptionLocalRepository
import javax.inject.Inject

class GetLocalInfoUseCase @Inject constructor(
    private val localRepository: DescriptionLocalRepository
){
    suspend fun getLocalInfo(id: Int) = localRepository.getInfo(id)
}