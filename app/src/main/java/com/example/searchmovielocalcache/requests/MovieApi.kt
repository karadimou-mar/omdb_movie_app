package com.example.searchmovielocalcache.requests

import androidx.lifecycle.LiveData
import com.example.searchmovielocalcache.models.Movie
import com.example.searchmovielocalcache.requests.responses.ApiResponse
import com.example.searchmovielocalcache.requests.responses.MovieDetailResponse
import com.example.searchmovielocalcache.requests.responses.MovieSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("/?type=movie")
    fun searchMovies(
        @Query("s") title: String,
        @Query("apikey") apikey: String,
        @Query("page") page: String
    ): LiveData<ApiResponse<MovieSearchResponse>>

    @GET("/?plot=full")
    fun getMovieByID(
        @Query("i") id: String,
        @Query("apikey") apikey: String
    ): LiveData<ApiResponse<MovieDetailResponse>>



}