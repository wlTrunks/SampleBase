package com.inter.trunks.data.movies.source.webservice

import com.inter.trunks.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class MoviesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val defaultRequest = chain.request()

        val defaultHttpUrl = defaultRequest.url()

        val httpUrl = defaultHttpUrl.newBuilder()
            .addQueryParameter(API_KEY_PR, BuildConfig.TOKEN_KEY)
            .build()

        val requestBuilder = defaultRequest.newBuilder()
            .url(httpUrl)

        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val API_KEY_PR = "api_key"
    }
}