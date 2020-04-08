package com.example.searchmovielocalcache

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesearchengine.util.VerticalSpacingItemDecorator
import com.example.searchmovielocalcache.adapters.MovieRecyclerAdapter
import com.example.searchmovielocalcache.adapters.OnMovieListener
import com.example.searchmovielocalcache.viewmodels.MainViewModel


class MainActivity : BaseActivity(), OnMovieListener {

    companion object {
        const val TAG = "MainActivity"
    }

    // components
    private lateinit var mSearchView: SearchView
    private lateinit var mRecipeListViewModel: MainViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMovieAdapter : MovieRecyclerAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents()
        initSearchView()
        initRecyclerView()
        subscribeObservers()
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    private fun initSearchView(){
        mSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun subscribeObservers(){

        mRecipeListViewModel.getViewState().observe(this, object: Observer<MainViewModel.ViewState>{
            override fun onChanged(viewState: MainViewModel.ViewState?) {
                if (viewState != null){
                    when (viewState){

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
    }

    private fun initRecyclerView(){
        mMovieAdapter = MovieRecyclerAdapter(this)
        val itemDecorator = VerticalSpacingItemDecorator(30)
        mRecyclerView.addItemDecoration(itemDecorator)
        mRecyclerView.adapter = mMovieAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(this)


    }

    private fun initComponents(){
        mSearchView = findViewById(R.id.search_view)
        mRecyclerView = findViewById(R.id.movie_list)
        mRecipeListViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onMovieClick(position: Int) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movie", mMovieAdapter.getSelectedMovie(position))
        startActivity(intent)
    }

    private fun displaySearchImage(){
        mMovieAdapter.displaySearchImage()
    }
}
