package com.example.searchmovielocalcache

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.example.recipesearchengine.util.VerticalSpacingItemDecorator
import com.example.searchmovielocalcache.adapters.MovieRecyclerAdapter
import com.example.searchmovielocalcache.adapters.OnMovieListener
import com.example.searchmovielocalcache.models.Movie
import com.example.searchmovielocalcache.utils.Resource
import com.example.searchmovielocalcache.viewmodels.MainViewModel
import com.example.searchmovielocalcache.viewmodels.MainViewModel.Companion.QUERY_EXHAUSTED


class MainActivity : BaseActivity(), OnMovieListener {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var mSearchView: SearchView
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMovieAdapter: MovieRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        initComponents()
        initSearchView()
        initRecyclerView()
        subscribeObservers()
        setSupportActionBar(findViewById(R.id.toolbar))
    }


    private fun subscribeObservers() {

        mMainViewModel.getViewState()
            .observe(this, object : Observer<MainViewModel.ViewState> {
                override fun onChanged(viewState: MainViewModel.ViewState?) {
                    if (viewState != null) {
                        when (viewState) {

                            MainViewModel.ViewState.IMAGE -> {
                                displaySearchImage()
                            }
                            MainViewModel.ViewState.MOVIES -> {
                                // from the other observer
                            }
                        }
                    }
                }

            })

        mMainViewModel.getMovies().observe(this, object : Observer<Resource<List<Movie>>> {
            override fun onChanged(listResource: Resource<List<Movie>>?) {
                if (listResource != null) {
                    if (listResource.data != null) {
                        Log.d(TAG, "onChanged: status: ${listResource.status}")

                        when (listResource.status) {

                            Resource.Status.SUCCESS -> {
                                Log.d(TAG, "onChanged: cache has been refreshed")
                                Log.d(
                                    TAG,
                                    "onChanged: status: SUCCESS, #movies: ${listResource.data.size}"
                                )
                                mMovieAdapter.hideLoading()
                                mMovieAdapter.setMovies(listResource.data as MutableList<Movie>)

                            }

                            Resource.Status.LOADING -> {
                                if (mMainViewModel.getPageNumber() > 1) {
                                    mMovieAdapter.displayLoading()
                                } else {
                                    mMovieAdapter.displayOnlyLoading()

                                }
                            }

                            Resource.Status.ERROR -> {

                                mMovieAdapter.hideLoading()
                                mMovieAdapter.setMovies(listResource.data as MutableList<Movie>)
                                Toast.makeText(
                                    this@MainActivity,
                                    listResource.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                                if (listResource.message == QUERY_EXHAUSTED) {
                                    mMovieAdapter.setQueryExhausted()
                                }

                            }
                        }

                    }
                }
            }
        })
    }

    fun searchMovieApi(search: String) {
        mRecyclerView.smoothScrollToPosition(0)
        mMainViewModel.searchMoviesApi(search, 1)
        mSearchView.clearFocus()
    }

    private fun initRecyclerView() {
        val viewPreloader: ViewPreloadSizeProvider<String> = ViewPreloadSizeProvider()

        mMovieAdapter = MovieRecyclerAdapter(this, initGlide(), viewPreloader)

        val preloader: RecyclerViewPreloader<String> = RecyclerViewPreloader(
            Glide.with(this),
            mMovieAdapter, viewPreloader,
            30
        )

        val itemDecorator = VerticalSpacingItemDecorator(30)
        mRecyclerView.addItemDecoration(itemDecorator)



        val linearLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = linearLayoutManager

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (mRecyclerView.canScrollVertically(1)
                    && mMainViewModel.getViewState().value == MainViewModel.ViewState.MOVIES){
                    mMainViewModel.searchNextPage()
                }
            }
        })

        mRecyclerView.addOnScrollListener(preloader)

        mRecyclerView.adapter = mMovieAdapter


    }

    private fun initSearchView() {

        mSearchView.isIconified = false

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchMovieApi(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun initGlide(): RequestManager {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.white_background)
            .error(R.drawable.white_background)

        return Glide.with(this).setDefaultRequestOptions(options)
    }

    private fun initComponents() {
        mSearchView = findViewById(R.id.search_view)
        mRecyclerView = findViewById(R.id.movie_list)

    }

    override fun onMovieClick(position: Int) {
        Log.d(TAG, "onMovieClick: $position item was clicked.")
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movie", mMovieAdapter.getSelectedMovie(position))
        startActivity(intent)
    }

    private fun displaySearchImage() {
        mMovieAdapter.displaySearchImage()
    }

    override fun onBackPressed() {
        if (mMainViewModel.getViewState().value == MainViewModel.ViewState.IMAGE) {
            super.onBackPressed()
        }else {
            mMainViewModel.cancelSearchRequest()
            mMainViewModel.setViewInitImage()
        }
    }
}
