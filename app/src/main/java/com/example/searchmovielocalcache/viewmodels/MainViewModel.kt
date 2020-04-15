package com.example.searchmovielocalcache.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.searchmovielocalcache.models.Movie
import com.example.searchmovielocalcache.repository.MovieRepository
import com.example.searchmovielocalcache.utils.Resource

class MainViewModel(application: Application): AndroidViewModel(application) {

    companion object{
        const val TAG = "MainViewModel"
        const val QUERY_EXHAUSTED = "No more results."
    }

    private var viewState: MutableLiveData<ViewState>? = null
    private var movies: MediatorLiveData<Resource<List<Movie>>> = MediatorLiveData()
    private var movieRepository: MovieRepository = MovieRepository.getInstance(application)!!
    private var isQueryExhausted: Boolean = false
    private var isPerformingQuery: Boolean = false
    private var cancelRequest: Boolean = false
    private var mPageNumber: Int = 0
    private var mSearch: String = ""
    private var requestStartTime: Long = 0L


    init {
        initState()
    }

    private fun initState(){
        if (viewState == null){
            viewState = MutableLiveData()
            viewState!!.value = ViewState.IMAGE
        }
    }

    fun getViewState(): LiveData<ViewState>{
        return viewState!!
    }

    fun getMovies(): LiveData<Resource<List<Movie>>>{
        return movies
    }
    fun getPageNumber(): Int {
        return mPageNumber
    }

    fun searchMoviesApi(search: String, _page: Int){
       var page = _page
        if (!isPerformingQuery){
            if (page == 0){
                page = 1
            }
            mSearch = search
            mPageNumber = page
            isQueryExhausted = false
            executeSearch()
        }
    }

    fun searchNextPage(){
        if (!isPerformingQuery && !isQueryExhausted){
            mPageNumber++
            executeSearch()
        }
    }

    private fun executeSearch() {

        isPerformingQuery = true
        viewState?.value = ViewState.MOVIES
        requestStartTime = System.currentTimeMillis()

        val repositorySource: LiveData<Resource<List<Movie>>> =
            movieRepository.searchMoviesApi(mSearch, mPageNumber)

        // When the data (repositorySource) is returned from the repository the onChanged method will be triggered
        movies.addSource(repositorySource, object : Observer<Resource<List<Movie>>> {
            override fun onChanged(listResource: Resource<List<Movie>>?) {

                if (!cancelRequest) {

                    if (listResource != null) {
                        movies.value = listResource
                        if (listResource.status == Resource.Status.SUCCESS) {
                            Log.d (TAG, "onChanged: REQUEST TIME: ${System.currentTimeMillis().minus(requestStartTime)/1000} seconds")
                            isPerformingQuery = false
                            if (listResource.data != null) {
                                if (listResource.data.isEmpty()) {
                                    Log.d(TAG, "executeSearch: onChange: query is exhausted..")
                                    movies.value = Resource<List<Movie>>(
                                        listResource.data,
                                        QUERY_EXHAUSTED,
                                        Resource.Status.ERROR
                                    )

                                }
                            }
                            movies.removeSource(repositorySource)
                        } else if (listResource.status == Resource.Status.ERROR) {
                            Log.d (TAG, "onChanged: REQUEST TIME: ${System.currentTimeMillis().minus(requestStartTime)/1000} seconds")

                            isPerformingQuery = false
                            movies.removeSource(repositorySource)
                        }
                    } else {
                        movies.removeSource(repositorySource)
                    }
                }
                else{
                    movies.removeSource(repositorySource)
                }
            }

        })
    }

    fun setViewInitImage(){
        viewState?.value = ViewState.IMAGE
    }

    fun cancelSearchRequest(){
        if (isPerformingQuery){
            cancelRequest = true
            isPerformingQuery = false
            mPageNumber = 1
        }
    }


    enum class ViewState{
        IMAGE, MOVIES
    }
}