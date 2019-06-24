package com.inter.trunks.domain.movies.usecases

import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.result.Either
import com.inter.trunks.domain.common.usecases.UseCase
import com.inter.trunks.domain.movies.entities.Movie
import com.inter.trunks.domain.movies.repository.IMovieDBRepository

class AddFavoriteMovieUC(
    private val repositoryI: IMovieDBRepository
) : UseCase<UseCase.None, AddFavoriteMovieUC.Param>() {
    override suspend fun run(params: Param): Either<BaseError, None> {
        repositoryI.addFavorite(params.movie)
        return Either.Right(None())
    }

    class Param(val movie: Movie)
}