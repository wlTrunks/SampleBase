package com.inter.trunks.data.movies.di

import androidx.room.Room
import com.inter.trunks.data.BuildConfig
import com.inter.trunks.data.movies.mapper.MovieDataMapper
import com.inter.trunks.data.movies.source.db.LocalMovieDBImpl
import com.inter.trunks.data.movies.source.db.MovieDB
import com.inter.trunks.data.common.network.Network.createApiClient
import com.inter.trunks.data.common.network.Network.createOkHttpClient
import com.inter.trunks.data.movies.source.webservice.MoviesInterceptor
import com.inter.trunks.data.movies.source.webservice.RemoteMovieDBImpl
import com.inter.trunks.data.movies.source.webservice.api.MovieDBApi
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val MoviesDataModule = module(override = true) {
    single { MovieDataMapper() }
    single {
        Room.databaseBuilder(androidApplication(), MovieDB::class.java, "movies_database").build()
    }

    //DB
    single { get<MovieDB>().getMovieDao() }
    single { LocalMovieDBImpl(get(), get()) }

    //Web
    single {
        createApiClient<MovieDBApi>(
            BuildConfig.BASE_URL,
            createOkHttpClient(MoviesInterceptor())
        )
    }
    single { RemoteMovieDBImpl(get(), get()) }
}