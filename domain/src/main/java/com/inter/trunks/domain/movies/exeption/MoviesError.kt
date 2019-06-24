package com.inter.trunks.domain.movies.exeption

import com.inter.trunks.domain.common.exception.FeatureFailure


data class EmptyDB(val message: String = "Empty DB") : FeatureFailure()

data class MoviesServerError(val message: String, val errorCode: Int) :
    FeatureFailure()