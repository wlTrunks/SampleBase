package com.inter.trunks.presentation.sample.moviedb

import androidx.lifecycle.MutableLiveData
import com.inter.trunks.domain.common.util.toLogcat
import com.inter.trunks.domain.movies.entities.Movie
import com.inter.trunks.domain.movies.usecases.GetPopularMoviesUC
import com.inter.trunks.presentation.base.vm.BaseViewModel
import java.lang.Exception

class PopularMoviesVM(private val popularMoviesUC: GetPopularMoviesUC) :
    BaseViewModel() {
    val popularMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    fun getPopularMovies() {
        runOnBackgound {
            try {
                val result = popularMoviesUC.run(GetPopularMoviesUC.Params(1, true))
                result.either(::handleFailure, ::handleMovieList)
            } catch (e: Exception) {
                e.toLogcat("PopularMoviesVM")
            }
        }
    }

    private fun handleMovieList(movies: List<Movie>) {
        runOnUI {
            popularMovies.postValue(movies)
        }
    }
}