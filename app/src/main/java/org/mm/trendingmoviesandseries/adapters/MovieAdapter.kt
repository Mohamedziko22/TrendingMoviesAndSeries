package org.mm.trendingmoviesandseries.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.mm.trendingmoviesandseries.R
import org.mm.trendingmoviesandseries.models.Movie

class MovieAdapter(
    private val context: Context,
    private var movies: ArrayList<Movie>,
    private val listener: onItemClickListener
) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie, position)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setList(movies: ArrayList<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
        private val imgFav: ImageView = itemView.findViewById(R.id.img_fav)
        private val imgPoster: ImageView = itemView.findViewById(R.id.img_poster)

        fun bind(movie: Movie, position: Int) {
            txtTitle.text = movie?.title
            if (movie?.favourite!!){
                imgFav.setImageResource(R.drawable.favorite_ic)
            }else{
                imgFav.setImageResource(R.drawable.not_favorite_ic)
            }
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/original" + movie?.backdrop_path)
                .into(imgPoster)

            // on item click
            itemView.setOnClickListener {
                listener.onItemClick(movie, position)
            }
            //on fav click
            imgFav.setOnClickListener {
                listener.onClickFav(movie, position)
            }
        }
    }

    interface onItemClickListener {
        fun onItemClick(movie: Movie, position: Int)
        fun onClickFav(movie: Movie, position: Int)
    }
}