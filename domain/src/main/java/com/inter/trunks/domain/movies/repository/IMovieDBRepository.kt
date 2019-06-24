package com.inter.trunks.domain.movies.repository

import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.result.Either
import com.inter.trunks.domain.movies.entities.Movie

interface IMovieDBRepository {
    suspend fun getRemotePopularMovies(page: Int): Either<BaseError, List<Movie>>
    suspend fun getLocalPopularMovies(page: Int): Either<BaseError, List<Movie>>
    suspend fun addFavorite(movie: Movie)
    suspend fun getFavoriteMovies(): Either<BaseError, List<Movie>>
}