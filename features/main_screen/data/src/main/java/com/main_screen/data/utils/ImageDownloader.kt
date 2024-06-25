package com.main_screen.data.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CancellationException

class ImageDownloader {
    fun downloadImage(directory: File, url: String): Flow<DownloadState> = flow {
        emit(DownloadState.Progress(0f)) // Emit initial progress

        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val fileName = url.substring(url.lastIndexOf('/') + 1)
                val file = File(directory, fileName)

                val inputStream: InputStream = connection.inputStream
                val totalSize = connection.contentLength
                val buffer = ByteArray(1024)
                var bytesRead: Int
                var downloadedSize = 0

                file.outputStream().use { outputStream ->
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        kotlinx.coroutines.delay(15)
                        outputStream.write(buffer, 0, bytesRead)
                        downloadedSize += bytesRead
                        val progress = (downloadedSize.toFloat() / totalSize)
                        emit(DownloadState.Progress(progress))
                    }
                }

                inputStream.close()
                emit(DownloadState.Success(file.path))
            } else {
                throw Exception("HTTP error code: ${connection.responseCode}")
            }
        } catch (e: Exception) {
            if (e is CancellationException)
                throw e
            emit(DownloadState.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}