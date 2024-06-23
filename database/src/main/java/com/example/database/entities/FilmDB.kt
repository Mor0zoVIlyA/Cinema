package com.example.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
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

@Entity(
    foreignKeys = [ForeignKey(
        entity = FilmDB::class,
        parentColumns = ["id"],
        childColumns = ["filmId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class FilmDescriptionEntity(
    @PrimaryKey
    val filmId: Int,
    val genre: String,
    val description: String,
    val country: String
)

data class FilmWithDescription(
    @Embedded val film: FilmDB,
    @Relation(
        parentColumn = "id",
        entityColumn = "filmId",
    )
    val description: FilmDescriptionEntity
)