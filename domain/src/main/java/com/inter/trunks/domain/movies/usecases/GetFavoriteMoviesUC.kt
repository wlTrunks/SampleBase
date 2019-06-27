package com.inter.trunks.domain.movies.usecases

import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.result.Either
import com.inter.trunks.domain.common.usecases.UseCase
import com.inter.trunks.domain.movies.entities.Movie
import com.inter.trunks.domain.movies.repository.IMovieDBRepository

class GetFavoriteMoviesUC(
    private val repositoryI: IMovieDBRepository
) : UseCase<List<Movie>, UseCase.None>() {
    override suspend fun run(params: None): Either<BaseError, List<Movie>> =
        repositoryI.getFavoriteMovies()
}