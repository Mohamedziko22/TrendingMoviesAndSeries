package org.mm.trendingmoviesandseries.ui.movie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mm.trendingmoviesandseries.R
import org.mm.trendingmoviesandseries.adapters.MovieAdapter
import org.mm.trendingmoviesandseries.databinding.ActivityMainBinding
import org.mm.trendingmoviesandseries.models.Movie
import org.mm.trendingmoviesandseries.networks.NetworkAvailable
import org.mm.trendingmoviesandseries.roomdb.AppDatabase
import org.mm.trendingmoviesandseries.roomdb.MovieDao
import org.mm.trendingmoviesandseries.ui.extradetails.ExtraDetailsActivity

class MainActivity : AppCompatActivity(), MovieAdapter.onItemClickListener {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var binding: ActivityMainBinding
    var movieList: ArrayList<Movie> = arrayListOf()
    val movieAdapter = MovieAdapter(this, movieList, this)
    var isInternetAvailable = false
    var roomDB: AppDatabase? = null
    var movieDao: MovieDao? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //view model
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        //instance of network availability
        isInternetAvailable = NetworkAvailable.isInternetAvailable(this)

        //instance of db
        roomDB = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-movie"
        ).build()

        //instance of dao
        movieDao = roomDB!!.movieDao()

        //build recycler movie
        buildRecycler()


    }

    private fun buildRecycler() {
        binding.recyclerMovie.layoutManager = LinearLayoutManager(this)
        binding.recyclerMovie.adapter = movieAdapter

        if (isInternetAvailable) {
            binding.bar.visibility = View.VISIBLE
            movieViewModel.fetchTrendingMovies()
            movieViewModel.movies.observe(this, { movies ->
                // invisable progress
                binding.bar.visibility = View.GONE

                //save in room and build recycler
                saveMovieInRoom(movies)
            })
        } else {
            showSnackbar(binding.recyclerMovie, getString(R.string.error_connection))
            //get when offline mode
            CoroutineScope(Dispatchers.IO).launch {
                if (movieDao?.getAll()?.isNotEmpty()!!) {
                    movieAdapter.setList(movieDao?.getAll()!! as ArrayList<Movie>)
                    movieAdapter.notifyDataSetChanged()
                } else {
                    showSnackbar(binding.recyclerMovie, getString(R.string.no_offline_db))
                }
            }
        }
    }

    private fun saveMovieInRoom(movies: ArrayList<Movie>?) {
        //set arraylist
        movieList = movies as ArrayList<Movie>

        CoroutineScope(Dispatchers.IO).launch {
            //select favourite and update new
            var listFav: ArrayList<Movie> = (movieDao?.loadAllByFav(true) as ArrayList<Movie>?)!!
            if (listFav.size > 0)
                listFav.forEach { m -> movieList?.find { m2 -> m.id == m2.id }?.favourite = true }

            //delete last data if found
            if (movieList?.isNotEmpty()!!) {
                movieDao?.deleteAll()
            }

            //save new
            for (i in 0 until movieList?.size!!)
                movieDao?.insertModel(movieList.get(i))
        }

        movieAdapter.setList(movieList)
        movieAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(movie: Movie, position: Int) {
        val intent = Intent(this, ExtraDetailsActivity::class.java)
        intent.putExtra("obj", movie)
        startActivity(intent)
    }

    override fun onClickFav(movie: Movie, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            //update item fav
            movieDao?.updateFav(!movie.favourite, movie.id)
            movie?.favourite = !movie.favourite
        }

        //rebuild recycler with update
        movieList.set(position, movie)
        movieAdapter.setList(movieList)
        movieAdapter.notifyDataSetChanged()

    }

    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }


}