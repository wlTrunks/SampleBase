package com.inter.trunks.domain.common.usecases

import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.result.Either

abstract class UseCase<out Type, in Params> where Type : Any {
    abstract suspend fun run(params: Params): Either<BaseError, Type>
    class None
}
