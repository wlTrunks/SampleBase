package com.inter.trunks.data.movies.source

import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.result.Either

interface MovieDBStore<out T> {
    suspend fun getPopularMovies(page: Int): Either<BaseError, T>
}