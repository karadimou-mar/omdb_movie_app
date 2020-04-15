package com.example.searchmovielocalcache.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

//@Entity(tableName = "ratings")
data class Rating(

////    @PrimaryKey
////    @NotNull
//    val rating_id: Int,

    //@ColumnInfo(name = "source")
    @SerializedName("Source")
    val source: String = "",

    //@ColumnInfo(name = "value")
    @SerializedName("Value")
    val value: String = ""

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(source)
        parcel.writeString(value)
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
