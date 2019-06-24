package com.inter.trunks.domain.common.result

sealed class BaseSuccess(val message: String? = null) {
    abstract class FeatureSuccess(message: String?) : BaseSuccess(message)
}