package com.example.searchmovielocalcache.requests.responses

import com.example.searchmovielocalcache.models.Rating
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieDetailResponse(

    @SerializedName("Rated")
    @Expose
    val rated: String = "",

    @SerializedName("Runtime")
    @Expose
    val runtime: String = "",

    @SerializedName("Genre")
    @Expose
    val genre: String = "",

    @SerializedName("Released")
    @Expose
    val released: String = "",

    @SerializedName("Plot")
    @Expose
    val plot: String = "",

    @SerializedName("Director")
    @Expose
    val director: String = "",

    @SerializedName("Writer")
    @Expose
    val writer: String = "",

    @SerializedName("Actors")
    @Expose
    val actor: String = "",

    @SerializedName("Metascore")
    @Expose
    val metascore: String = "",

    @SerializedName("imdbRating")
    @Expose
    val imdbRating: String = "",

    @SerializedName("Ratings")
    @Expose
    val rating: List<Rating> = emptyList()
)