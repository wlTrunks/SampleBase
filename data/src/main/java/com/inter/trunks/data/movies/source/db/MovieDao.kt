package com.inter.trunks.data.movies.source.db

import androidx.room.*
import com.inter.trunks.data.common.dao.BaseDao
import com.inter.trunks.data.movies.entities.MovieData

@Dao
interface MovieDao : BaseDao<MovieData> {

    @Query("SELECT * FROM all_movies")
    suspend fun getAllMovies(): List<MovieData>

    @Query("SELECT * FROM all_movies LIMIT :limit OFFSET :offset")
    suspend fun getPopularMovies(limit: Int = 10, offset: Int): List<MovieData>

    @Query("DELETE FROM all_movies")
    suspend fun clear()

    @Query("SELECT * FROM all_movies WHERE isFavorite = 1")
    suspend fun getFavoritesMovies(): List<MovieData>

}