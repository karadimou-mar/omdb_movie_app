package com.example.searchmovielocalcache.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.searchmovielocalcache.R
import com.example.searchmovielocalcache.models.Movie

class MovieRecyclerAdapter(onMovieListener: OnMovieListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "MovieRecyclerAdapter"
        private const val MOVIE_TYPE = 1
        private const val LOADING_TYPE = 2
        private const val IMAGE_TYPE = 3
        private const val EXHAUSTED_TYPE = 4
    }

    private var mMovies: MutableList<Movie> = ArrayList()
    private val mOnMovieListener: OnMovieListener = onMovieListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View?

        when (viewType) {

            MOVIE_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_movie_list_item,parent,false)
                return MovieViewHolder(view, mOnMovieListener)
            }
            LOADING_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_loading_list_item,parent,false)
                return LoadingViewHolder(view)
            }
            EXHAUSTED_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_search_exhausted, parent, false)
                return SearchExhaustedViewHolder(view)
            }
            IMAGE_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_initial_image_item, parent, false)
                return InitImageViewHolder(view)
            }

            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_movie_list_item,parent,false)
                return MovieViewHolder(view, mOnMovieListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return mMovies.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemViewType = getItemViewType(position)

        if (itemViewType == MOVIE_TYPE) {
            val requestOptions: RequestOptions = RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background)

            Glide.with(holder.itemView.context)
                .setDefaultRequestOptions(requestOptions)
                .load(mMovies[position].poster)
                .into((holder as MovieViewHolder).image)
        }

    }

    override fun getItemViewType(position: Int): Int {

        return when (mMovies[position].title) {
            "IMAGE" -> {
                IMAGE_TYPE
            }
            "LOADING" -> {
                LOADING_TYPE
            }
            "EXHAUSTED" -> {
                EXHAUSTED_TYPE
            }
            else -> {
                MOVIE_TYPE
            }
        }
    }

    private fun setQueryExhausted(){

    }

    private fun hideLoading(){

    }

    private fun displayLoading(){

    }

    private fun isLoading(): Boolean{
        return false
    }


    fun setMovies(movies: MutableList<Movie>){
        mMovies = movies
        notifyDataSetChanged()
    }

    fun getSelectedMovie(position: Int): Movie? {
            if (mMovies.size > 0) {
                return mMovies[position]
            }
            return null
      }

    fun displaySearchImage(){
        val list: MutableList<Movie> = ArrayList()
        val movie = Movie()
        movie.title = "IMAGE"
        list.add(movie)
        mMovies = list
        notifyDataSetChanged()
    }

}