package com.example.searchmovielocalcache.requests.responses

import com.example.searchmovielocalcache.requests.MovieApi
import com.example.searchmovielocalcache.utils.Constants.BASE_URL
import com.example.searchmovielocalcache.utils.Constants.CONNECTION_TIMEOUT
import com.example.searchmovielocalcache.utils.Constants.READ_TIMEOUT
import com.example.searchmovielocalcache.utils.Constants.WRITE_TIMEOUT
import com.example.searchmovielocalcache.utils.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceGenerator {

    private val client: OkHttpClient = OkHttpClient.Builder()

        // establish connection to server
        .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        // time between each byte read from the server
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        // time between each byte sent to server
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build()

    private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())


    private val retrofit: Retrofit = retrofitBuilder.build()

    private val movieApi: MovieApi = retrofit.create(MovieApi::class.java)

    //public method for accessing the above
    fun getMovieApi(): MovieApi{
        return movieApi
    }


}
