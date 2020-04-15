package com.example.searchmovielocalcache.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "movies")
data class Movie(

    @SerializedName("imdbID")
    @Expose
    @PrimaryKey
    @NotNull
    var imdbID: String = "",

    @SerializedName("Title")
    @Expose
    @ColumnInfo(name = "title")
    var title: String = "",

    @SerializedName("Year")
    @Expose
    @ColumnInfo(name = "year")
    var year: String = "",

    @SerializedName("Type")
    @Expose
    @ColumnInfo(name = "type")
    var type: String = "",

    @SerializedName("Poster")
    @Expose
    @ColumnInfo(name = "poster")
    var poster: String = "",

    @ColumnInfo(name = "rated")
    var rated: String = "",

    @ColumnInfo(name = "runtime")
    var runtime: String = "",

    @ColumnInfo(name = "genre")
    var genre: String = "",

    @ColumnInfo(name = "released")
    var released: String = "",

    @ColumnInfo(name = "plot")
    var plot: String = "",

    @ColumnInfo(name = "director")
    var director: String = "",

    @ColumnInfo(name = "writer")
    val writer: String = "",

    @ColumnInfo(name = "actor")
    var actor: String = "",

    @ColumnInfo(name ="imdbrating")
    var imdbRating: String = "",

    @ColumnInfo(name = "metascore")
    var metascore: String = "",

    @ColumnInfo(name = "rating")
    var rating: List<Rating> = emptyList(),

    @ColumnInfo(name = "response")
    var response: String = "",

    @ColumnInfo(name = "timestamp")
    var timestamp: Int = 0

) : Parcelable {
    constructor(parcel: Parcel) : this(
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
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Rating)!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imdbID)
        parcel.writeString(title)
        parcel.writeString(year)
        parcel.writeString(type)
        parcel.writeString(poster)
        parcel.writeString(rated)
        parcel.writeString(runtime)
        parcel.writeString(genre)
        parcel.writeString(released)
        parcel.writeString(plot)
        parcel.writeString(director)
        parcel.writeString(writer)
        parcel.writeString(actor)
        parcel.writeString(imdbRating)
        parcel.writeString(metascore)
        parcel.writeTypedList(rating)
        parcel.writeString(response)
        parcel.writeInt(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}