package com.description_screen.data.di

import com.description_screen.data.DescriptionLocalRepositoryImpl
import com.description_screen.data.DescriptionRemoteRepositoryImpl
import com.description_screen.domain.DescriptionLocalRepository
import com.description_screen.domain.DescriptionRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
    @Binds
    fun bindLocal(descriptionLocalRepository: DescriptionLocalRepositoryImpl): DescriptionLocalRepository
    @Binds
    fun bindRemote(descriptionLocalRepository: DescriptionRemoteRepositoryImpl): DescriptionRemoteRepository
}