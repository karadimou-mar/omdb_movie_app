package com.example.searchmovielocalcache

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.searchmovielocalcache.models.Movie
import com.example.searchmovielocalcache.utils.Resource
import com.example.searchmovielocalcache.viewmodels.MovieDetailViewModel

class MovieDetailActivity : BaseActivity() {

    companion object {
        const val TAG = "MovieDetailActivity"
    }

    private lateinit var mMovieDetailViewModel: MovieDetailViewModel
    private lateinit var mScrollView: ScrollView
    private lateinit var mMovieImage: AppCompatImageView
    private lateinit var mMovieTitle: TextView
    private lateinit var mMoviePlotContainer: LinearLayout
    private lateinit var mMovieWriterContainer: LinearLayout
    private lateinit var mMovieActorContainer: LinearLayout
    private lateinit var mMovieDirectorContainer: LinearLayout
    private lateinit var mMovieDetailsContainer: LinearLayout
    private lateinit var mImdbScore: TextView
    private lateinit var mRottenScore: TextView
    private lateinit var mCriticScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        mMovieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)

        initComponents()
        showProgressBar(true)
        getIncomingIntent()

    }

    private fun subscribeObservers(id: String) {
        mMovieDetailViewModel.searchSingleMovieApi(id)
            .observe(this, object : Observer<Resource<Movie>> {
                override fun onChanged(movieDetailResource: Resource<Movie>?) {

                    if (movieDetailResource != null) {
                        if (movieDetailResource.data != null) {
                            Log.d(TAG, "onChanged: status: ${movieDetailResource.status}")

                            when (movieDetailResource.status) {

                                Resource.Status.LOADING -> {
                                    showProgressBar(true)
                                }

                                Resource.Status.SUCCESS -> {
                                    Log.d(TAG, "onChanged: cache has been refreshed.")
                                    Log.d(
                                        TAG,
                                        "onChanged: status: SUCCESS, Director: ${movieDetailResource.data.director}"
                                    )
                                    showParent()
                                    showProgressBar(false)
                                    //set the widgets - in progress
                                    setMovieProperties(movieDetailResource.data)
                                }

                                Resource.Status.ERROR -> {
                                    Log.e(
                                        TAG,
                                        "onChanged: status: ERROR, Movie: ${movieDetailResource.data.title}"
                                    )
                                    Log.e(
                                        TAG,
                                        "onChanged: ERROR message: ${movieDetailResource.message}"
                                    )
                                    //showParent()
                                    showProgressBar(false)
                                    //set the widgets - in  progress
                                    //setMovieProperties(movieDetailResource.data)
                                    //display error screen - in progress
                                    displayErrorToast()
                                }

                            }
                        }
                    }
                }

            })
    }

    private fun setMovieProperties(movie: Movie?) {

        if (movie != null) {
            val options: RequestOptions = RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background)

            Glide.with(this)
                .setDefaultRequestOptions(options)
                .load(movie.poster)
                .into(mMovieImage)


            mMovieTitle.text = "${movie.title}  (${movie.year})"
            mImdbScore.text = "IMDb:\n ${movie.imdbRating}"
            mCriticScore.text = "Metacritic:\n ${movie.metascore}"
            setMovieRTScore(movie)
            setMoviePlot(movie)
            setMovieWriter(movie)
            setMovieActor(movie)
            setMovieDirector(movie)
            setMovieDetail(movie)
        }
    }

    private fun setMoviePlot(movie: Movie?) {
        mMoviePlotContainer.removeAllViews()
        val textView = TextView(this)
        if (movie?.plot != null) textView.text = movie.plot else textView.text = "N/A"
        textView.textSize = 15f
        textView.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mMoviePlotContainer.addView(textView)
    }

    private fun setMovieWriter(movie: Movie?) {
        mMovieWriterContainer.removeAllViews()
        val textView = TextView(this)
        if (movie?.writer != null) textView.text = movie.writer else textView.text = "N/A"
        textView.textSize = 15f
        textView.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mMovieWriterContainer.addView(textView)
    }

    private fun setMovieActor(movie: Movie?) {
        mMovieActorContainer.removeAllViews()
        val textView = TextView(this)
        if (movie?.actor != null) textView.text = movie.actor else textView.text = "N/A"
        textView.textSize = 15f
        textView.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mMovieActorContainer.addView(textView)
    }

    private fun setMovieDirector(movie: Movie?) {
        mMovieDirectorContainer.removeAllViews()
        val textView = TextView(this)
        if (movie?.director != null) textView.text = movie.director else textView.text = "N/A"
        textView.textSize = 15f
        textView.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mMovieDirectorContainer.addView(textView)
    }

    private fun setMovieDetail(movie: Movie?) {
        mMovieDetailsContainer.removeAllViews()
        val textView = TextView(this)
        textView.text = "${movie?.rated} | ${movie?.runtime} | ${movie?.genre} | ${movie?.released}"
        textView.textSize = 15f
        textView.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mMovieDetailsContainer.addView(textView)

    }

    private fun displayErrorToast() {
        Toast.makeText(
            this, "Error retrieving information.\nPlease check your connection.",
            Toast.LENGTH_SHORT
        ).show()

    }


    private fun setMovieRTScore(movie: Movie?) {
        var count = 0
        for (i in movie?.rating!!.indices) {
            if (movie.rating[i].source == "Rotten Tomatoes") {
                mRottenScore.text = "Rotten Tomatoes: ${movie.rating[i].value}"
                count++
            } else if (count != 1) {
                mRottenScore.text = "Rotten Tomatoes: N/A"
            }

        }

    }

    private fun getIncomingIntent() {
        if (intent.hasExtra("movie")) {
            val movie: Movie = intent.getParcelableExtra("movie")!!
            Log.d(TAG, "getIncomingIntent: ${movie.title}")
            //searchSingleMovieApi(movie.title)
            subscribeObservers(movie.imdbID)
        }
    }


    private fun initComponents() {
        mScrollView = findViewById(R.id.parent)
        mMovieImage = findViewById(R.id.movie_image)
        mMovieTitle = findViewById(R.id.movie_title)
        mMoviePlotContainer = findViewById(R.id.plot_container)
        mMovieWriterContainer = findViewById(R.id.writer_container)
        mMovieActorContainer = findViewById(R.id.actor_container)
        mMovieDirectorContainer = findViewById(R.id.director_container)
        mMovieDetailsContainer = findViewById(R.id.details_container)
        mImdbScore = findViewById(R.id.imdb_score)
        mRottenScore = findViewById(R.id.rt_score)
        mCriticScore = findViewById(R.id.ms_score)

    }

    private fun showParent() {
        mScrollView.visibility = View.VISIBLE
    }
}
