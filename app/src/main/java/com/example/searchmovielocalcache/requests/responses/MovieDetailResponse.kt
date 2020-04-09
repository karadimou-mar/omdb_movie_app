package com.example.searchmovielocalcache.requests.responses

import com.example.searchmovielocalcache.models.Rating
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieDetailResponse(

    @SerializedName("Rated")
    @Expose
    var rated: String = "",

    @SerializedName("Runtime")
    @Expose
    var runtime: String = "",

    @SerializedName("Genre")
    @Expose
    var genre: String = "",

    @SerializedName("Released")
    @Expose
    var released: String = "",

    @SerializedName("Plot")
    @Expose
    var plot: String = "",

    @SerializedName("Director")
    @Expose
    var director: String = "",

    @SerializedName("Writer")
    @Expose
    var writer: String = "",

    @SerializedName("Actors")
    @Expose
    var actor: String = "",

    @SerializedName("Metascore")
    @Expose
    var metascore: String = "",

    @SerializedName("imdbRating")
    @Expose
    var imdbRating: String = "",

    @SerializedName("Ratings")
    @Expose
    var rating: List<Rating> = emptyList()
)