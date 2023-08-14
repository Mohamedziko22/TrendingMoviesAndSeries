package org.mm.trendingmoviesandseries.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mm.trendingmoviesandseries.models.Movie
import org.mm.trendingmoviesandseries.roomdb.MovieDao

class MovieViewModel : ViewModel() {
    private val movieRepository = MovieRepository()

    private val moviesMutableLiveData = MutableLiveData<ArrayList<Movie>>()
    val movies: LiveData<ArrayList<Movie>> get() = moviesMutableLiveData

    fun fetchTrendingMovies() {
        viewModelScope.launch {
            try {
                val trendingMovies = movieRepository.getTrendingMovies()
                moviesMutableLiveData.value = trendingMovies
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}