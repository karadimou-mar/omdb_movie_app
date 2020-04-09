package com.example.searchmovielocalcache

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    // components
    private lateinit var mSearchView: SearchView
    private lateinit var mRecipeListViewModel: MainViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMovieAdapter: MovieRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents()
        initSearchView()
        initRecyclerView()
        subscribeObservers()
        setSupportActionBar(findViewById(R.id.toolbar))
    }


    private fun subscribeObservers() {

        mRecipeListViewModel.getViewState()
            .observe(this, object : Observer<MainViewModel.ViewState> {
                override fun onChanged(viewState: MainViewModel.ViewState?) {
                    if (viewState != null) {
                        when (viewState) {

                            MainViewModel.ViewState.IMAGE -> {
                                displaySearchImage()
                            }
                            MainViewModel.ViewState.MOVIES -> {
                                // in progress
                            }
                        }
                    }
                }

            })

        mRecipeListViewModel.getMovies().observe(this, object : Observer<Resource<List<Movie>>> {
            override fun onChanged(listResource: Resource<List<Movie>>?) {
                if (listResource != null) {
                    if (listResource.data != null) {
                        Log.d(TAG, "onChanged: status: ${listResource.status}")

                        when (listResource.status) {

                            Resource.Status.SUCCESS -> {
                                Log.d(TAG, "onChanged: cache has been refreshed")
                                Log.d(
                                    TAG,
                                    "onChanged: status: SUCCESS, #recipes: ${listResource.data.size}"
                                )
                                mMovieAdapter.hideLoading()
                                mMovieAdapter.setMovies(listResource.data as MutableList<Movie>)

                            }

                            Resource.Status.LOADING -> {
                                if (mRecipeListViewModel.getPageNumber() > 1) {
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

                                if (listResource.message == QUERY_EXHAUSTED){
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
        mRecipeListViewModel.searchMoviesApi(search, 1)
        mSearchView.clearFocus()
    }

    private fun initRecyclerView() {
        mMovieAdapter = MovieRecyclerAdapter(this)
        val itemDecorator = VerticalSpacingItemDecorator(30)
        mRecyclerView.addItemDecoration(itemDecorator)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mMovieAdapter


    }

    private fun initSearchView() {
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

    private fun initComponents() {
        mSearchView = findViewById(R.id.search_view)
        mRecyclerView = findViewById(R.id.movie_list)
        mRecipeListViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onMovieClick(position: Int) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movie", mMovieAdapter.getSelectedMovie(position))
        startActivity(intent)
    }

    private fun displaySearchImage() {
        mMovieAdapter.displaySearchImage()
    }
}
