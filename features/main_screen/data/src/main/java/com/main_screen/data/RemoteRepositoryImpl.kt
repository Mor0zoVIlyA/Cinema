package com.main_screen.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.main_screen.data.api.ServiceApi
import com.main_screen.data.mapper.toFilmCard
import com.main_screen.data.network_model.Film
import com.main_screen.domain.FilmCard
import com.main_screen.domain.RemoteRepository
import com.main_screen.domain.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import java.util.concurrent.CancellationException
import javax.inject.Inject
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class RemoteRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit,
    @ApplicationContext private val context: Context
) : RemoteRepository {
    override suspend fun fetchFilms(filmRequestType: String): Result<List<FilmCard>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val serviceApi = retrofit.create(ServiceApi::class.java)
                val resultList = serviceApi.getTopFilms(filmRequestType).films.map { film: Film ->
                    film.toFilmCard()
                }
                Result.Success(resultList)
            } catch (e: Exception) {
                if (e is CancellationException)
                    throw e
                Result.Error(e)
            }
        }

    override fun internetConnectionFlow() = callbackFlow {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        val isConnected = connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } ?: false

        trySend(isConnected)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}