package org.mm.trendingmoviesandseries.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title")
    @SerializedName("name") val title: String,
    @ColumnInfo(name = "original_title")
    @SerializedName("original_name") val original_title: String,
    @ColumnInfo(name = "overview")
    @SerializedName("overview") val overview: String,
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path") val poster_path: String,
    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path") val backdrop_path: String,
    @ColumnInfo(name = "favourite") var favourite: Boolean = false
) : Serializable