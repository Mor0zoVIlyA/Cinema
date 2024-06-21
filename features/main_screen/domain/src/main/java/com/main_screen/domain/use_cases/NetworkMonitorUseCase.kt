package com.main_screen.domain.use_cases

import com.main_screen.domain.RemoteRepository
import javax.inject.Inject

class NetworkMonitorUseCase @Inject constructor(private val repository: RemoteRepository) {
    fun isNetworkAvailable() = repository.internetConnectionFlow()
}