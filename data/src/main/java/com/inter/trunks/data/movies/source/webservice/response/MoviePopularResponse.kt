package com.inter.trunks.data.movies.source.webservice.response

import com.inter.trunks.data.movies.entities.MovieData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoviePopularResponse(
    @Json(name = "results") val popularMovieList: List<MovieData>? = null,
    @Json(name = "total_pages") val totalPage: Int
) : BaseMovieResponse()