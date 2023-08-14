package org.mm.trendingmoviesandseries.networks

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.mm.trendingmoviesandseries.models.Movie
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MovieApiClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val API_KEY = "fcd536f2f7ac2e3cbaf9c870da6c8997"

    private val retrofit: Retrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private val apiService: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }

    suspend fun fetchTrendingMovies(): ArrayList<Movie> {
        val response = apiService.getTrendingMovies(API_KEY)
        return response.body()?.results ?: arrayListOf()
    }


}