package com.example.searchmovielocalcache.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "movies")
data class Movie(

    @PrimaryKey
    @NotNull
    var id: Int = 0 ,

    @ColumnInfo(name = "title")
    @SerializedName("Title")
    var title: String = "",

    @ColumnInfo(name = "year")
    @SerializedName("Year")
    var year: String = "",

    @ColumnInfo(name = "imdbID")
    @SerializedName("imdbID")
    var imdbID: String = "",

    @ColumnInfo(name = "type")
    @SerializedName("Type")
    var type: String = "",

    @ColumnInfo(name = "poster")
    @SerializedName("Poster")
    var poster: String = "",

    @ColumnInfo(name = "timestamp")
    var timestamp: Int = 0

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(year)
        parcel.writeString(imdbID)
        parcel.writeString(type)
        parcel.writeString(poster)
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


