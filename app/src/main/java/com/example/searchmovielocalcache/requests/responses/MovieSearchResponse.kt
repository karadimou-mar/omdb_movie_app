package com.example.searchmovielocalcache.requests.responses

import com.example.searchmovielocalcache.models.Movie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieSearchResponse(

    @SerializedName("totalResults")
    @Expose
    val totalResults: String,

    @SerializedName("Response")
    @Expose
    val response: String,

    @SerializedName("Search")
    @Expose
    val movies: List<Movie>
)

