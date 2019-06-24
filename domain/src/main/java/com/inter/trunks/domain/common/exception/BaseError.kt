package com.inter.trunks.domain.common.exception



sealed class BaseError {
    abstract class FeatureError(val msg: String? = null) : BaseError()
    class ExceptionError(val e: Exception) : BaseError()

    object NetworkConnection : BaseError()
    object Unauthorizes : BaseError()
    object ServerError : BaseError()
}

abstract class FeatureFailure : BaseError()
class ExceptionError(val e: Exception) : BaseError()