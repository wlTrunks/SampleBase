package com.inter.trunks.data.movies.source.db

import com.inter.trunks.data.movies.source.LocalMovieDBStore
import com.inter.trunks.data.movies.entities.MovieData
import com.inter.trunks.data.movies.mapper.MovieDataMapper
import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.result.Either
import com.inter.trunks.domain.movies.entities.Movie
import com.inter.trunks.domain.movies.exeption.EmptyDB

class LocalMovieDBImpl(
    private val movieDao: MovieDao,
    private val mapper: MovieDataMapper
) :
    LocalMovieDBStore {
    override suspend fun getPopularMovies(page: Int): Either<BaseError, List<Movie>> =
        either(movieDao.getPopularMovies(offset = page))


    override suspend fun getFavoriteMovies(): Either<BaseError, List<Movie>> =
        either(movieDao.getFavoritesMovies())


    override suspend fun addFavorite(movie: Movie) {
        movieDao.update(mapper.mapTo(movie))
    }

    private fun either(list: List<MovieData>): Either<BaseError, List<Movie>> {
        return if (list.isNotEmpty()) Either.Right(mapper.mapListFrom(list))
        else Either.Left(EmptyDB())
    }
}