package com.example.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "film")
data class FilmDB (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val year: String,
    val name: String,
    val path: String
)

@Entity
data class FilmDescriptionEntity(
    @PrimaryKey
    val filmId: Int,
    val genre: String,
    val country: String
)

data class FilmWithDescription(
    @Embedded val film: FilmDB,
    @Relation(
        parentColumn = "id",
        entityColumn = "filmId"
    )
    val description: FilmDescriptionEntity
)