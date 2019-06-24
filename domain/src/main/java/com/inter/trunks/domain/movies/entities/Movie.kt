package com.inter.trunks.domain.movies.entities

data class Movie(
    val id: Long,
    val voteAverage: Float = 0.0f,
    val title: String,
    val poster: String,
    val overview: String,
    val releaseDate: String,
    var isFavorite: Boolean = false
)
