package com.example.database.entities

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FilmDB::class, FilmDescriptionEntity::class], version = 1)
abstract class FilmDataBase : RoomDatabase() {
    abstract fun filmDao(): FilmEntityDao
}