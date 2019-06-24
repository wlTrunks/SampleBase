package com.inter.trunks.data.movies

import com.inter.trunks.data.movies.source.db.LocalMovieDBImpl
import com.inter.trunks.data.movies.source.webservice.RemoteMovieDBImpl
import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.result.Either
import com.inter.trunks.domain.movies.entities.Movie
import com.inter.trunks.domain.movies.repository.IMovieDBRepository

class IMovieDBRepositoryImpl(
    private val remote: RemoteMovieDBImpl,
    private val local: LocalMovieDBImpl
) : IMovieDBRepository {
    override suspend fun getRemotePopularMovies(page: Int): Either<BaseError, List<Movie>> {
        return remote.getPopularMovies(page)
    }

    override suspend fun getLocalPopularMovies(page: Int): Either<BaseError, List<Movie>> {
        return local.getPopularMovies(page)
    }

    override suspend fun addFavorite(movie: Movie) {
        local.addFavorite(movie)
    }

    override suspend fun getFavoriteMovies(): Either<BaseError, List<Movie>> =
        local.getFavoriteMovies()
}