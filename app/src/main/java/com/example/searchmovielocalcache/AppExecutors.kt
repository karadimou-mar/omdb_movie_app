package com.example.searchmovielocalcache

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class AppExecutors {


    companion object {

        @Volatile
        private var instance: AppExecutors? = null

        fun getInstance(): AppExecutors {
            if (instance == null) {
                instance = AppExecutors()
            }
            return instance as AppExecutors
        }
    }

    private val mNetworkIO = Executors.newScheduledThreadPool(3)

    fun networkIO(): ScheduledExecutorService{
        return mNetworkIO
    }
}
