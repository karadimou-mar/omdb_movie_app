package com.example.searchmovielocalcache.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.searchmovielocalcache.AppExecutors
import com.example.searchmovielocalcache.models.Movie
import com.example.searchmovielocalcache.persistence.MovieDao
import com.example.searchmovielocalcache.persistence.MovieDatabase
import com.example.searchmovielocalcache.requests.responses.ApiResponse
import com.example.searchmovielocalcache.requests.responses.MovieSearchResponse
import com.example.searchmovielocalcache.requests.responses.ServiceGenerator
import com.example.searchmovielocalcache.utils.Constants.API_KEY
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
        const val TAG = "RecipeRepository"


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
        NetworkBoundResource<List<Movie>, MovieSearchResponse>(AppExecutors.getInstance()){
            override fun saveCallResult(item: MovieSearchResponse) {

                val movies: Array<Movie> = Array(item.movies.size){Movie()}
                var index = 0

                for (rowId: Long in movieDao.insertMovies(item.movies.toTypedArray())){
                    if (rowId == -1L){
                       Log.d(TAG, "saveCallResult: CONFLICT.. This recipe is already in cache")
                        movieDao.updateMovie(
                            movies[index].title,
                            movies[index].year,
                            movies[index].poster,
                            movies[index].type,
                            movies[index].imdbID
                        )
                    }
                    index++
                }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Movie>> {
                return movieDao.searchMovies(search,page)
            }

            override fun createCall(): LiveData<ApiResponse<MovieSearchResponse>> {
               return ServiceGenerator.getMovieApi().searchMovies(search,API_KEY,page.toString())
            }

        }.asLiveData()
    }
}