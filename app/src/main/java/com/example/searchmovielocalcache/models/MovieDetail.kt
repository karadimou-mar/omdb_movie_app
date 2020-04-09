package com.example.searchmovielocalcache.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "movie_detail")
data class MovieDetail(

    @PrimaryKey
    @NotNull
    val detailId: Int,

    @ColumnInfo(name = "rated")
    val rated: String = "",

    @ColumnInfo(name = "runtime")
    val runtime: String = "",

    @ColumnInfo(name = "genre")
    val genre: String = "",

    @ColumnInfo(name = "released")
    val released: String = "",

    @ColumnInfo(name = "plot")
    val plot: String = "",

    @ColumnInfo(name = "director")
    val director: String = "",

    @ColumnInfo(name = "writer")
    val writer: String = "",

    @ColumnInfo(name = "actor")
    val actor: String = "",

    @ColumnInfo(name = "metascore")
    val metascore: String = "",

    @ColumnInfo(name = "imdbRating")
    val imdbRating: String = "",

    @ColumnInfo(name = "rating")
    val rating: List<Rating> = emptyList(),

    @ColumnInfo(name = "timestamp")
    val timestamp: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Rating)!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(detailId)
        parcel.writeString(rated)
        parcel.writeString(runtime)
        parcel.writeString(genre)
        parcel.writeString(released)
        parcel.writeString(plot)
        parcel.writeString(director)
        parcel.writeString(writer)
        parcel.writeString(actor)
        parcel.writeString(metascore)
        parcel.writeString(imdbRating)
        parcel.writeTypedList(rating)
        parcel.writeInt(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieDetail> {
        override fun createFromParcel(parcel: Parcel): MovieDetail {
            return MovieDetail(parcel)
        }

        override fun newArray(size: Int): Array<MovieDetail?> {
            return arrayOfNulls(size)
        }
    }
}