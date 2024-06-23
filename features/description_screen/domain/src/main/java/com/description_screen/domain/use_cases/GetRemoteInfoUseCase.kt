package com.description_screen.domain.use_cases

import com.description_screen.domain.DescriptionRemoteRepository
import javax.inject.Inject

class GetRemoteInfoUseCase @Inject constructor(
    private val remoteRepository: DescriptionRemoteRepository
) {
    suspend fun fetchRemoteInfo(id: Int) = remoteRepository.fetchInfo(id)
}