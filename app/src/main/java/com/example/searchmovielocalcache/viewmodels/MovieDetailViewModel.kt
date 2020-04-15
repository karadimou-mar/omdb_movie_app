package com.example.searchmovielocalcache.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.searchmovielocalcache.models.Movie
import com.example.searchmovielocalcache.repository.MovieRepository
import com.example.searchmovielocalcache.utils.Resource

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "MovieDetailViewModel"
    }

    private var movieRepository: MovieRepository = MovieRepository.getInstance(application)!!

    fun searchSingleMovieApi(title: String): LiveData<Resource<Movie>>{
        return movieRepository.searchSingleMovieApi(title)
    }
}