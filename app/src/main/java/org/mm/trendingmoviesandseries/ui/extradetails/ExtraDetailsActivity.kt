package org.mm.trendingmoviesandseries.ui.extradetails

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mm.trendingmoviesandseries.R
import org.mm.trendingmoviesandseries.databinding.ActivityExtraDetailsBinding
import org.mm.trendingmoviesandseries.databinding.ActivityMainBinding
import org.mm.trendingmoviesandseries.models.Movie
import org.mm.trendingmoviesandseries.roomdb.AppDatabase
import org.mm.trendingmoviesandseries.roomdb.MovieDao

class ExtraDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExtraDetailsBinding
    var movie: Movie? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtraDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get movie object from explicit intent
        if (intent.hasExtra("obj")) {
            movie = intent.getSerializableExtra("obj") as? Movie
            binding.txtTitle.text = movie?.title
            binding.txtOriginalTitle.text = movie?.original_title
            binding.txtOverview.text = movie?.overview

            if (movie?.favourite!!) {
                binding.imgFav.setImageResource(R.drawable.favorite_ic)
            } else {
                binding.imgFav.setImageResource(R.drawable.not_favorite_ic)
            }

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/original" + movie?.backdrop_path)
                .into(binding.imgPoster)

        } else {
            showSnackbar(binding.imgPoster, getString(R.string.no_data_found))
        }
    }

    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

}