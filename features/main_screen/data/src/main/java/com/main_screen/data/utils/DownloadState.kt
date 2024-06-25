package com.main_screen.data.utils

sealed class DownloadState {
    data class Progress(val percentage: Float) : DownloadState()
    data class Success(val filePath: String) : DownloadState()
    data class Error(val exception: Exception) : DownloadState()
}
