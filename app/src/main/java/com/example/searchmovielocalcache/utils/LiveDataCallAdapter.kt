package com.example.searchmovielocalcache.utils

import androidx.lifecycle.LiveData
import com.example.searchmovielocalcache.requests.responses.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class LiveDataCallAdapter<R>(private val responseType: Type):
    CallAdapter<R, LiveData<ApiResponse<R>>> {

    companion object{
        const val TAG = "LiveDataCallAdapter"
    }


    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {
        return object: LiveData<ApiResponse<R>>(){
            override fun onActive() {
                super.onActive()

                val apiResponse: ApiResponse<R> = ApiResponse()
                call.clone().enqueue(object: Callback<R> {
                    override fun onFailure(call: Call<R>, t: Throwable) {
                        postValue(apiResponse.create(t))
                    }

                    override fun onResponse(call: Call<R>, response: Response<R>) {
                        postValue(apiResponse.create(response))


                    }

                })
            }
        }
    }

    override fun responseType(): Type = responseType


}