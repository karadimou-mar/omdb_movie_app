package com.example.searchmovielocalcache.repository

import android.content.Context
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

    private var recipeDao: MovieDao

    init {
        recipeDao = MovieDatabase.getInstance(context)!!.getMovieDao()
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

    fun searchRecipesApi(search: String, page: Int): LiveData<Resource<List<Movie>>> {

        return object :
        NetworkBoundResource<List<Movie>, MovieSearchResponse>(AppExecutors.getInstance()){
            override fun saveCallResult(item: MovieSearchResponse) {

            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Movie>> {
                return recipeDao.searchMovies(search,page)
            }

            override fun createCall(): LiveData<ApiResponse<MovieSearchResponse>> {
               return ServiceGenerator.getMovieApi()
                   .getMovies(search,API_KEY,page)
            }

        }.asLiveData()
    }
}