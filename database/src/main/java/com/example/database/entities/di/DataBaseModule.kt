package com.example.database.entities.di

import android.content.Context
import androidx.room.Room
import com.example.database.entities.FilmDataBase
import com.example.database.entities.FilmEntityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    @Singleton
    fun providesDataBase(@ApplicationContext context: Context): FilmDataBase {
        return Room.databaseBuilder(
            context,
            FilmDataBase::class.java,
            "film-database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesDao(filmDataBase: FilmDataBase): FilmEntityDao{
        return filmDataBase.filmDao()
    }

}