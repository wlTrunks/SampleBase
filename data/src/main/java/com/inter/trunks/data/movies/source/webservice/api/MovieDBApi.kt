package com.inter.trunks.data.movies.source.webservice.api

import com.inter.trunks.data.movies.source.webservice.response.MoviePopularResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDBApi {
    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun getPopularMovies(@Query("page") page: Int): MoviePopularResponse
}