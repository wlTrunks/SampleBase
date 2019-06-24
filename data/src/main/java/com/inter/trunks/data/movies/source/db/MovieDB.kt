package com.inter.trunks.data.movies.source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inter.trunks.data.movies.entities.MovieData

@Database(entities = [MovieData::class], version = 1)
abstract class MovieDB : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}