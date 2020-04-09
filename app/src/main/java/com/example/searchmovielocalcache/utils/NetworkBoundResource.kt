package com.example.searchmovielocalcache.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.example.searchmovielocalcache.AppExecutors
import com.example.searchmovielocalcache.requests.responses.ApiResponse
import com.example.searchmovielocalcache.requests.responses.MovieSearchResponse


// CacheObject: Type for the Resource data. (ResultType)
// RequestObject: Type for the API response. (RequestType)
abstract class NetworkBoundResource<CacheObject, RequestObject>
constructor(private var appExecutors: AppExecutors) {

    companion object {
        const val TAG = "NetworkBoundResource"
    }

    private val results: MediatorLiveData<Resource<CacheObject>> = MediatorLiveData()

    init {
        initialize()
    }

    private fun initialize() {
        //update LiveData for loading status
        results.value = Resource.Loading(null)

        //observe LiveData source from local db
        val dbSource: LiveData<CacheObject> = loadFromDb()

        results.addSource(dbSource, object : Observer<CacheObject> {
            override fun onChanged(cacheObject: CacheObject) {

                results.removeSource(dbSource)

                if (shouldFetch(cacheObject)) {
                    //fetch data from api
                    fetchFromNetwork(dbSource)

                } else {
                    //get data from cache
                    results.addSource(dbSource, object : Observer<CacheObject> {
                        override fun onChanged(cacheObject: CacheObject) {
                            setValue(Resource.Success(cacheObject))
                        }
                    })
                }
            }
        })
    }

    private fun fetchFromNetwork(dbSource: LiveData<CacheObject>) {

        Log.d(TAG, "fetchFromNetwork: called")

        //update LiveData for loading status
        results.addSource(dbSource, object : Observer<CacheObject> {
            override fun onChanged(cacheObject: CacheObject) {
                setValue(Resource.Loading(cacheObject))
            }
        })

        val apiResponse: LiveData<ApiResponse<RequestObject>> = createCall()

        results.addSource(apiResponse, object : Observer<ApiResponse<RequestObject>> {
            override fun onChanged(requestObjectApiResponse: ApiResponse<RequestObject>?) {
                results.removeSource(dbSource)
                results.removeSource(apiResponse)

                /**
                 *  3 cases:
                 *
                 *  1) ApiSuccessResponse
                 *  2) ApiErrorResponse
                 *  3) ApiEmptyResponse
                 */

                if (requestObjectApiResponse is ApiResponse<*>.ApiSuccessResponse<*>) {

                    Log.d(TAG, "onChanged: ApiSuccessResponse.")

                    appExecutors.diskIO()?.execute(object : Runnable {
                        override fun run() {

                            //save the response to the local db
                            saveCallResult((processResponse(requestObjectApiResponse as ApiResponse<*>.ApiSuccessResponse<*>)) as RequestObject)

                            appExecutors.mainThread()!!.execute(object : Runnable {
                                override fun run() {
                                    results.addSource(loadFromDb(), object : Observer<CacheObject> {
                                        override fun onChanged(cacheObject: CacheObject) {
                                            setValue(Resource.Success(cacheObject))
                                        }
                                    })
                                }
                            })
                        }
                    })
                }
                else if (requestObjectApiResponse is ApiResponse<*>.ApiEmptyResponse<*>){

                    Log.d(TAG, "onChanged: ApiEmptyResponse")

                    appExecutors.mainThread()?.execute(object : Runnable{
                        override fun run() {
                            results.addSource(loadFromDb(), object :Observer<CacheObject>{
                                override fun onChanged(cacheObject: CacheObject) {
                                    setValue(Resource.Success(cacheObject))
                                }

                            })
                        }

                    })
                }
                else if (requestObjectApiResponse is ApiResponse<*>.ApiErrorResponse<*>){

                    Log.d(TAG, "onChanged: ApiErrorResponse.")

                    results.addSource(dbSource, object : Observer<CacheObject>{
                        override fun onChanged(cacheObject: CacheObject) {
                            setValue(
                                Resource.Error(requestObjectApiResponse.errorMessage.toString(), cacheObject)
                            )
                        }

                    })
                }
            }

        })
    }

    private fun processResponse(response: ApiResponse<*>.ApiSuccessResponse<*>): CacheObject{
        return response.body as CacheObject
    }

    private fun setValue(newValue: Resource<CacheObject>) {
        if (results.value != newValue) {
            results.value = newValue
        }
    }

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestObject)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: CacheObject?): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<CacheObject>

    // Called to create the API call.
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestObject>>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected open fun onFetchFailed() {}

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    fun asLiveData(): LiveData<Resource<CacheObject>> {
        return results
    }

}