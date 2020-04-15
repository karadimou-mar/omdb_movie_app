package com.example.searchmovielocalcache.requests.responses

import android.util.Log
import retrofit2.Response
import java.io.IOException

open class ApiResponse<T> {

    companion object{
        const val TAG = "ApiResponse"
    }


    /**
     * Generic success response from api
     * @param <T>
     */
    inner class ApiSuccessResponse<T> internal constructor(val body: T) : ApiResponse<T>()


    /**
     * Generic Error response from API
     * @param <T>
     */
    inner class ApiErrorResponse<T> internal constructor(val errorMessage: String?) :
        ApiResponse<T>()

    /**
     * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
     */
    inner class ApiEmptyResponse<T> : ApiResponse<T>()



    fun <T> create(error: Throwable): ApiResponse<T> {
        return if (error.message != "") {
            ApiErrorResponse(error.message)
        } else {
            ApiErrorResponse("Unknown error\nCheck network connection")
        }
    }



    fun <T> create(response: Response<T>): ApiResponse<T> {
        if (response.isSuccessful) {
            val body: T = response.body()!!
            Log.d("ApiResponse", "$body ")

//            if (body is MovieSearchResponse){
//                if (!CheckMovieApiKey.isMovieApiKeyValid(body as MovieSearchResponse)){
//                    val errorMessage = "Api key is invalid or expired!"
//                    return ApiErrorResponse(errorMessage)
//                }
//            }
//
//            if (body is MovieDetailResponse) {
//                if (!CheckMovieApiKey.isMovieApiKeyValid(body as MovieDetailResponse)){
//                    val errorMessage = "Api key is invalid or expired"
//                    return ApiErrorResponse(errorMessage)
//                }
//            }

            // 204 code is empty response
            if (body == null || response.code() == 204) {
                response.raw().body?.close()
                return ApiEmptyResponse()
            } else {

                return ApiSuccessResponse(body)
            }

        }
        else {
            val errorMsg: String
            errorMsg = try {
                response.errorBody().toString()
            } catch (e: IOException) {
                e.printStackTrace()
                response.message()
            }
            return ApiErrorResponse(errorMsg)
        }
    }
}