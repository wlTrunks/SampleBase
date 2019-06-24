package com.inter.trunks.data.movies.source.webservice.response

import com.squareup.moshi.Json

const val OK_SERVER_CODE = -1

open class BaseMovieResponse {
    @Json(name = "status_code")
    var statusCode: Int = OK_SERVER_CODE
    @Json(name = "status_message")
    var statusMessage = ""
    @Json(name = "success")
    var success = true
}