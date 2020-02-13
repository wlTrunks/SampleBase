package com.inter.trunks.domain.movies.usecases

import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.result.Either
import com.inter.trunks.domain.common.usecases.UseCase
import com.inter.trunks.domain.common.util.toLogcat
import com.inter.trunks.domain.movies.entities.Movie
import com.inter.trunks.domain.movies.repository.IMovieDBRepository

class GetPopularMoviesUC(
    private val repositoryI: IMovieDBRepository
) : UseCase<List<Movie>, GetPopularMoviesUC.Params>() {

    override suspend fun run(params: Params): Either<BaseError, List<Movie>> {
        return if (params.remote) repositoryI.getRemotePopularMovies(params.page) else
            repositoryI.getLocalPopularMovies(params.page)
    }

    data class Params(val page: Int, val remote: Boolean)
}
