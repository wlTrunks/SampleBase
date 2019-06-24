package com.inter.trunks.moviedb.di

import com.inter.trunks.data.movies.IMovieDBRepositoryImpl
import com.inter.trunks.domain.movies.repository.IMovieDBRepository
import com.inter.trunks.domain.movies.usecases.GetPopularMoviesUC
import com.inter.trunks.presentation.sample.moviedb.PopularMoviesVM
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val MoviesModule = module(override = true) {
    single { IMovieDBRepositoryImpl(get(), get()) as IMovieDBRepository }
    single { GetPopularMoviesUC(get()) }
    viewModel { PopularMoviesVM(get()) }
}