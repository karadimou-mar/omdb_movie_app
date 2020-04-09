package com.example.searchmovielocalcache.utils

import androidx.lifecycle.LiveData
import com.example.searchmovielocalcache.requests.responses.ApiResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory: CallAdapter.Factory() {

    /**
     * This method performs a number of checks and then returns the Response type for the retrofit requests.
     * (@bodyType is the ResponseType. It can be RecipeResponse or RecipeSearchResponse)
     *
     * CHECK #1   returnType returns LiveData
     * CHECK #2   Type LiveData<T> is of ApiResponse.class
     * CHECK #3   Make sure ApiResponse is parameterized. AKA: ApiResponse<T> exists
     *
     */

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // CHECK #1
        // Make sure the CallAdapter is returning a type of LiveData
        if (getRawType(returnType) != LiveData::class.java){
            return null
        }

        // CHECK #2
        // Type that LiveData is wrapping
        // We need to see what kind of LiveData is the <T>, we have to go inside the LiveData
        val observableType: Type = getParameterUpperBound(0, returnType as ParameterizedType) //that line is getting <T>

        // check if T is of Type ApiResponse
        val rawObservable: Type = getRawType(observableType)
        if (rawObservable != ApiResponse::class.java){
            throw IllegalArgumentException("type must be a defined resource")
        }

        // CHECK #3
        // Check if ApiResponse is parameterized. AKA: Does ApiResponse<T> exists? (must wrap around T)
        // FYI: T is either RecipeResponse or T will be a RecipeSearchResponse
        if (observableType !is ParameterizedType){
            throw IllegalArgumentException("resource must be parameterized")
        }

        val bodyType: Type = getParameterUpperBound(0, observableType)
        return LiveDataCallAdapter<Type>(bodyType)


    }
}