package com.description_screen.data.di

import com.description_screen.data.network.NetworkApi
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetModule {
    @Provides
    fun provideApi(retrofit: Retrofit) : NetworkApi{
        return retrofit.create(NetworkApi::class.java)
    }
}