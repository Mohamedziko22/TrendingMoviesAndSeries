package org.mm.trendingmoviesandseries.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.mm.trendingmoviesandseries.models.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movies WHERE favourite = (:fav)")
    fun loadAllByFav(fav: Boolean): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModel(vararg movies: Movie)

    @Delete
    fun delete(user: Movie)

    @Query("DELETE FROM movies")
    fun deleteAll()

    @Query("UPDATE movies SET favourite = (:fav) WHERE id = (:id)")
    fun updateFav(fav: Boolean, id: Int)

}