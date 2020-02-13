package com.inter.trunks.moviedb.popular

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.inter.trunks.domain.movies.entities.Movie
import com.inter.trunks.moviedb.R
import com.inter.trunks.moviedb.base.ui.BaseFragment
import com.inter.trunks.presentation.sample.moviedb.PopularMoviesVM
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel

class MoviePopularFragment : BaseFragment() {

    private val vm: PopularMoviesVM by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_popular_movies

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.popularMovies.observe(this@MoviePopularFragment, Observer<List<Movie>> {
            context?.toast("movies size ${it.size}")
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener { vm.getPopularMovies() }
        cancel.setOnClickListener { vm.cancelRequest() }
    }
}