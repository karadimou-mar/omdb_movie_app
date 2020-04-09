package com.example.searchmovielocalcache.requests

import androidx.lifecycle.LiveData
import com.example.searchmovielocalcache.requests.responses.ApiResponse
import com.example.searchmovielocalcache.requests.responses.MovieDetailResponse
import com.example.searchmovielocalcache.requests.responses.MovieSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("/?type=movie")
    fun getMovies(
        @Query("s") title: String,
        @Query("apikey") apikey: String,
        @Query("page") page: Int
    ): LiveData<ApiResponse<MovieSearchResponse>>

    @GET("/?plot=full")
    fun getMovieDetails(
        @Query("t") title: String,
        @Query("apikey") apikey: String
    ): LiveData<ApiResponse<MovieDetailResponse>>



}