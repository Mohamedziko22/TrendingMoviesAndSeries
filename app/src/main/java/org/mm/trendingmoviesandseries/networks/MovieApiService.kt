package org.mm.trendingmoviesandseries.networks

import org.mm.trendingmoviesandseries.ui.movie.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("trending/tv/day")
    suspend fun getTrendingMovies(@Query("api_key") apiKey: String): Response<MovieResponse>
}