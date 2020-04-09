package com.example.searchmovielocalcache.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "ratings")
data class Rating(

    @PrimaryKey
    @NotNull
    val rating_id: Int,

    @ColumnInfo(name = "source")
    @SerializedName("Source")
    val source: String = "",

    @ColumnInfo(name = "value")
    @SerializedName("Value")
    val value: String = "",

    @ColumnInfo(name = "timestamp")
    val timestamp: Int = 0

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(source)
        parcel.writeString(value)
        parcel.writeInt(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Rating> {
        override fun createFromParcel(parcel: Parcel): Rating {
            return Rating(parcel)
        }

        override fun newArray(size: Int): Array<Rating?> {
            return arrayOfNulls(size)
        }
    }
}
