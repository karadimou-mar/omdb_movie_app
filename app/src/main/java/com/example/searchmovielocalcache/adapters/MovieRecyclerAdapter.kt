package com.example.searchmovielocalcache.adapters

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.example.searchmovielocalcache.R
import com.example.searchmovielocalcache.models.Movie
import java.util.*
import kotlin.collections.ArrayList

class MovieRecyclerAdapter(
    onMovieListener: OnMovieListener,
    val requestManager: RequestManager,
    val viewPreloadSizeProvider: ViewPreloadSizeProvider<String>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ListPreloader.PreloadModelProvider<String> {

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
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_movie_list_item, parent, false)
                return MovieViewHolder(
                    view,
                    mOnMovieListener,
                    requestManager,
                    viewPreloadSizeProvider
                )
            }
            LOADING_TYPE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_loading_list_item, parent, false)
                return LoadingViewHolder(view)
            }
            EXHAUSTED_TYPE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_search_exhausted, parent, false)
                return SearchExhaustedViewHolder(view)
            }
            IMAGE_TYPE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_initial_image_item, parent, false)
                return InitImageViewHolder(view)
            }

            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_movie_list_item, parent, false)
                return MovieViewHolder(
                    view,
                    mOnMovieListener,
                    requestManager,
                    viewPreloadSizeProvider
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return mMovies.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemViewType = getItemViewType(position)

        if (itemViewType == MOVIE_TYPE) {
            (holder as MovieViewHolder).onBind(mMovies[position])
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

    fun setQueryExhausted() {
        hideLoading()
        val exhaustedMovie = Movie()
        exhaustedMovie.title = "EXHAUSTED"
        mMovies.add(exhaustedMovie)
        notifyDataSetChanged()
    }

    fun hideLoading() {
        if (isLoading()) {
            if (mMovies[0].title == "LOADING") {
                mMovies.removeAt(mMovies.size - 1)
            } else if (mMovies[mMovies.size - 1].title == "LOADING") {
                mMovies.removeAt(mMovies.size - 1)
            }
        }
        notifyDataSetChanged()
    }

    fun displayLoading() {
        if (!isLoading()) {
            val movie = Movie()
            movie.title = "LOADING"
            mMovies.add(movie)
            notifyDataSetChanged()
        }
    }

    fun displayOnlyLoading() {
        clearMoviesList()
        val movie = Movie()
        movie.title = "LOADING"
        mMovies.add(movie)
        notifyDataSetChanged()
    }

    private fun isLoading(): Boolean {
        return false
    }

    private fun clearMoviesList() {
        mMovies.clear()
        notifyDataSetChanged()
    }


    fun setMovies(movies: MutableList<Movie>) {
        mMovies = movies
        notifyDataSetChanged()
    }

    fun getSelectedMovie(position: Int): Movie {
        if (mMovies.isNotEmpty()) {
            return mMovies[position]
        }
        return Movie()
    }

    fun displaySearchImage() {
        val list: MutableList<Movie> = ArrayList()
        val movie = Movie()
        movie.title = "IMAGE"
        list.add(movie)
        mMovies = list
        notifyDataSetChanged()
    }

    override fun getPreloadItems(position: Int): MutableList<String> {
        val url = mMovies[position].poster
        if (TextUtils.isEmpty(url)){
            return Collections.emptyList()
        }
        return Collections.singletonList(url)
    }

    override fun getPreloadRequestBuilder(item: String): RequestBuilder<*>? {
        return requestManager.load(item)
    }

}