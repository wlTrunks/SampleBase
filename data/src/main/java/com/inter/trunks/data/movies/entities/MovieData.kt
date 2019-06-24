package com.inter.trunks.data.movies.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "all_movies")
data class MovieData(
    @Json(name = "id") @PrimaryKey val id: Long,
    @Json(name = "vote_average") val voteAverage: Float = 0.0f,
    @Json(name = "title") val title: String,
    @Json(name = "video") val video: Boolean = false,
    @Json(name = "poster_path") val poster: String,
    @Json(name = "overview") val overview: String,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "is_favorite") var isFavorite: Boolean = false
)

//    "vote_count": 1009,
//    "id": 420817,
//    "video": false,
//    "vote_average": 7.3,
//    "title": "Aladdin",
//    "popularity": 499.517,
//    "poster_path": "/3iYQTLGoy7QnjcUYRJy4YrAgGvp.jpg",
//    "original_language": "en",
//    "original_title": "Aladdin",
//    "genre_ids": [
//    12,
//    14,
//    10749,
//    35,
//    10751
//    ],
//    "backdrop_path": "/v4yVTbbl8dE1UP2dWu5CLyaXOku.jpg",
//    "adult": false,
//    "overview": "A kindhearted street urchin named Aladdin embarks on a magical adventure after finding a lamp that releases a wisecracking genie while a power-hungry Grand Vizier vies for the same lamp that has the power to make their deepest wishes come true.",
//    "release_date": "2019-05-22"
//}
