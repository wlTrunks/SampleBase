package com.inter.trunks.data.movies.mapper

import com.inter.trunks.data.movies.entities.MovieData
import com.inter.trunks.domain.movies.entities.Movie
import com.inter.trunks.domain.common.mapper.Mapper

class MovieDataMapper : Mapper<MovieData, Movie> {

    override fun mapFrom(from: MovieData): Movie {
        with(from) {
            return Movie(
                id = id,
                voteAverage = voteAverage,
                title = title,
                poster = poster,
                overview = overview,
                releaseDate = releaseDate,
                isFavorite = isFavorite
            )
        }
    }


    override fun mapTo(from: Movie): MovieData {
        with(from) {
            return MovieData(
                id = id,
                voteAverage = voteAverage,
                title = title,
                poster = poster,
                overview = overview,
                releaseDate = releaseDate,
                isFavorite = isFavorite
            )
        }
    }
}
