package com.main_screen.domain.states

sealed class DownloadProgress {
    data class Progress(val progress: Float): DownloadProgress()
    data object Success : DownloadProgress()
    data class Error(val message: String): DownloadProgress()
}