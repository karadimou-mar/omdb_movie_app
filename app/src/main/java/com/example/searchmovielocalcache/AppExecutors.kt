package com.example.searchmovielocalcache

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

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

    private val mDiskIO: Executor = Executors.newSingleThreadExecutor()

    private val mMainThreadExecutor: Executor = MainThreadExecutor()


    fun diskIO(): Executor? {
        return mDiskIO
    }

    fun mainThread(): Executor? {
        return mMainThreadExecutor
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
