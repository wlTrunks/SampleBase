package com.inter.trunks.moviedb

import android.app.Application
import com.inter.trunks.data.movies.di.MoviesDataModule
import com.inter.trunks.moviedb.di.MoviesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(MoviesModule,
                MoviesDataModule)
        }
    }
}