package com.example.searchmovielocalcache.utils

object Constants {

    const val BASE_URL: String = "http://www.omdbapi.com"
    const val API_KEY: String = "2ced8c09"

    const val CONNECTION_TIMEOUT = 10L //10 seconds
    const val READ_TIMEOUT = 3L
    const val WRITE_TIMEOUT = 3L

    const val MOVIE_REFRESH_TIME = 60 * 60 * 24 * 30 // 30 days in seconds
    const val TEST_REFRESH_TIME = 1 * 60

}