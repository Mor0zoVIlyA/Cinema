package com.main_screen.data.di

import com.main_screen.data.LocalRepositoryImpl
import com.main_screen.data.RemoteRepositoryImpl
import com.main_screen.data.api.ServiceApi
import com.main_screen.domain.LocalRepository
import com.main_screen.domain.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule  {
    @Binds
    fun bindRemoteRepository(repositoryImpl: RemoteRepositoryImpl): RemoteRepository
    @Binds
    fun bindLocalRepository(repositoryImpl: LocalRepositoryImpl): LocalRepository
}