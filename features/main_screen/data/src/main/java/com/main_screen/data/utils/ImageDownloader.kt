package com.main_screen.data.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CancellationException

class ImageDownloader {
    suspend fun downloadImage(directory: File, url: String): String = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()
            val fileName = url.substring(url.lastIndexOf('/') + 1)
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream: InputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                val resultPath = File(directory, fileName).path
                saveBitmapToFile(bitmap, resultPath)
                return@withContext resultPath
            }
        } catch (e: Exception) {
            if (e is CancellationException)
                throw e
        }
        return@withContext ""
    }

    private fun saveBitmapToFile(bitmap: Bitmap, filePath: String) {
        val file = File(filePath).apply {
            if (exists()) {
                delete()
            }
            createNewFile()
        }
        val outputStream = FileOutputStream(file)
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            outputStream.close()
        }
    }
}