package com.example.searchmovielocalcache.requests.responses

import com.example.searchmovielocalcache.requests.MovieApi
import com.example.searchmovielocalcache.utils.Constants.BASE_URL
import com.example.searchmovielocalcache.utils.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {

    private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())


    private val retrofit: Retrofit = retrofitBuilder.build()

    private val movieApi: MovieApi = retrofit.create(MovieApi::class.java)

    //public method for accessing the above
    fun getMovieApi(): MovieApi{
        return movieApi
    }
}
