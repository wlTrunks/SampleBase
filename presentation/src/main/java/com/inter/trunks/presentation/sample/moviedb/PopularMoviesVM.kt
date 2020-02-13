package com.inter.trunks.presentation.sample.moviedb

import androidx.lifecycle.MutableLiveData
import com.inter.trunks.domain.movies.entities.Movie
import com.inter.trunks.domain.movies.usecases.GetPopularMoviesUC
import com.inter.trunks.presentation.base.vm.BaseViewModel
import kotlinx.coroutines.Deferred

class PopularMoviesVM(private val popularMoviesUC: GetPopularMoviesUC) :
    BaseViewModel() {

    val popularMovies: MutableLiveData<List<Movie>> = MutableLiveData()

    private var _getMoviesListJob: Deferred<Unit>? = null

    fun getPopularMovies() {
        _getMoviesListJob = runOnBackground {
            val result = popularMoviesUC.run(GetPopularMoviesUC.Params(1, true))
                .either(::handleFailure, ::handleMovieList)

        }
    }

    fun cancelRequest() {
        _getMoviesListJob?.cancel()
    }

    private fun handleMovieList(movies: List<Movie>) {
        runOnUI {
            popularMovies.postValue(movies)
        }
    }
}