package com.inter.trunks.moviedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.inter.trunks.moviedb.base.extension.addFragment
import com.inter.trunks.moviedb.popular.MoviePopularFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment(MoviePopularFragment(), R.id.container)
    }
}
