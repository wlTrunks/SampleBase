package com.inter.trunks.data.movies.source.webservice

import com.inter.trunks.data.movies.mapper.MovieDataMapper
import com.inter.trunks.data.movies.source.MovieDBStore
import com.inter.trunks.data.movies.source.webservice.api.MovieDBApi
import com.inter.trunks.data.movies.source.webservice.response.OK_SERVER_CODE
import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.result.Either
import com.inter.trunks.domain.common.util.toLogcat
import com.inter.trunks.domain.movies.entities.Movie
import com.inter.trunks.domain.movies.exeption.MoviesServerError

class RemoteMovieDBImpl(
    private val service: MovieDBApi,
    private val mapper: MovieDataMapper
) :
    MovieDBStore<List<Movie>> {

    override suspend fun getPopularMovies(page: Int): Either<BaseError, List<Movie>> {
        return try {
            val response = service.getPopularMovies(page)
            val movieList = mapper.mapListFrom(response.popularMovieList!!)
            "response movieList ${movieList.size}".toLogcat("RemoteMovieDBImpl")
            when (response.statusCode == OK_SERVER_CODE) {
                true -> Either.Right(movieList)
                false -> Either.Left(MoviesServerError(response.statusMessage, response.statusCode))
            }
        } catch (exception: Exception) {
            exception.toLogcat("RemoteMovieDBImpl")
            Either.Left(BaseError.ExceptionError(exception))
        }
    }
}