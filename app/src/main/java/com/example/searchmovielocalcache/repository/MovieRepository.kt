package com.example.searchmovielocalcache.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.searchmovielocalcache.AppExecutors
import com.example.searchmovielocalcache.models.Movie
import com.example.searchmovielocalcache.persistence.MovieDao
import com.example.searchmovielocalcache.persistence.MovieDatabase
import com.example.searchmovielocalcache.requests.responses.ApiResponse
import com.example.searchmovielocalcache.requests.responses.MovieDetailResponse
import com.example.searchmovielocalcache.requests.responses.MovieSearchResponse
import com.example.searchmovielocalcache.requests.responses.ServiceGenerator
import com.example.searchmovielocalcache.utils.Constants.API_KEY
import com.example.searchmovielocalcache.utils.Constants.TEST_REFRESH_TIME
import com.example.searchmovielocalcache.utils.NetworkBoundResource
import com.example.searchmovielocalcache.utils.Resource


/**
 *  CacheObject   -> List<Movie>
 *  RequestObject -> MovieSearchResponse
 */

class MovieRepository(context: Context) {

    private var movieDao: MovieDao

    init {
        movieDao = MovieDatabase.getInstance(context)!!.getMovieDao()
    }

    companion object {
        const val TAG = "MovieRepository"


        private var instance: MovieRepository? = null

        fun getInstance(context: Context): MovieRepository? {
            if (instance == null) {
                instance = MovieRepository(context)
            }
            return instance
        }
    }

    fun searchMoviesApi(search: String, page: Int): LiveData<Resource<List<Movie>>> {

        return object :
            NetworkBoundResource<List<Movie>, MovieSearchResponse>(AppExecutors.getInstance()) {
            override fun saveCallResult(item: MovieSearchResponse) {

                val movies: Array<Movie>? = Array(item.movies.size) { Movie() }
                var index = 0

                for (rowId: Long in movieDao.insertMovies(item.movies.toTypedArray())) {
                    if (rowId == -1L) {
                        Log.d(TAG, "saveCallResult: CONFLICT.. This movie is already in cache")
                        movieDao.updateMovie(
                            movies?.get(index)!!.title,
                            movies[index].year,
                            movies[index].poster,
                            movies[index].type,
                            movies[index].imdbID,
                            movies[index].plot
                        )
                    }
                    index++
                }

            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Movie>> {
                return movieDao.searchMovies(search, page)
            }

            override fun createCall(): LiveData<ApiResponse<MovieSearchResponse>> {
                return ServiceGenerator.getMovieApi().searchMovies(search, API_KEY, page.toString())
            }

        }.asLiveData()
    }

    fun searchSingleMovieApi(id: String): LiveData<Resource<Movie>> {

        return object :
            NetworkBoundResource<Movie, MovieDetailResponse>(AppExecutors.getInstance()) {
            override fun saveCallResult(item: MovieDetailResponse) {

                movieDao.updateMovieTimestamp((System.currentTimeMillis() / 1000).toInt(), id)

                movieDao.insertMovieDetails(
                    Movie()
                )

                movieDao.updateMovieDetails(
                    item.title,
                    item.rated,
                    item.runtime,
                    item.genre,
                    item.released,
                    item.plot,
                    item.director,
                    item.writer,
                    item.actor,
                    item.metascore,
                    item.rating,
                    item.imdbRating,
                    item.imdbID
                )

                Log.d(TAG, "saveCallResult2: $movieDao")
            }

            override fun shouldFetch(data: Movie?): Boolean {
                Log.d(TAG, "shouldFetch2: ${data.toString()}")

                val currentTime = (System.currentTimeMillis() / 1000).toInt()
                Log.d(TAG, "shouldFetch: current time: $currentTime")
                val lastRefresh = data?.timestamp
                Log.d(TAG, "shouldFetch: last refresh(timestamp): $lastRefresh")
                Log.d(TAG, "shouldFetch: it's been ${(currentTime.minus(lastRefresh!!) / 60) } minutes since this movie was refresh." +
                        " 1 minute must elapse before refreshing.")

                if (currentTime.minus(data.timestamp) >= TEST_REFRESH_TIME ){
                    Log.d(TAG, "shouldFetch: SHOULD REFRESH MOVIE? " +true)
                    return true
                }
                Log.d(TAG, "shouldFetch: SHOULD REFRESH MOVIE? " +false)
                return false

            }

            override fun loadFromDb(): LiveData<Movie> {
                return movieDao.getMovieByID(id)
            }

            override fun createCall(): LiveData<ApiResponse<MovieDetailResponse>> {
                Log.d(TAG, "createCall2: call created")
                return ServiceGenerator.getMovieApi().getMovieByID(id, API_KEY)
            }

        }.asLiveData()
    }
}
















