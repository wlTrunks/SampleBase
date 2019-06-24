package com.inter.trunks.data.movies.source

import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.result.Either
import com.inter.trunks.domain.movies.entities.Movie

interface LocalMovieDBStore : MovieDBStore<List<Movie>> {

    suspend fun addFavorite(movie: Movie)

    suspend fun getFavoriteMovies(): Either<BaseError, List<Movie>>
}