package org.mm.trendingmoviesandseries.ui.movie

import org.mm.trendingmoviesandseries.models.Movie
import org.mm.trendingmoviesandseries.networks.MovieApiClient
import org.mm.trendingmoviesandseries.roomdb.MovieDao

class MovieRepository {
    suspend fun getTrendingMovies(): ArrayList<Movie> {
        return MovieApiClient.fetchTrendingMovies()
    }

    suspend fun getTrendingMoviesOffline(movieDao: MovieDao): ArrayList<Movie> {
        return movieDao.getAll() as ArrayList<Movie>
    }

}