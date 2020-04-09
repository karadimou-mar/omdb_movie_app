package com.example.searchmovielocalcache.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.searchmovielocalcache.models.Movie
import com.example.searchmovielocalcache.models.MovieDetail
import com.example.searchmovielocalcache.models.Rating

@Dao
interface MovieDao {

    /**
     * if there is NO conflict, it will return a long[] of recipes ids
     * if there is, the specific id in the long[] will be -1
     */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovies(movies: Array<Movie>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieDetails(movie: MovieDetail)


    // Custom update queries so timestamp don't get removed
    @Query("UPDATE movies SET title = :title, year = :year, type = :type, poster = poster, imdbId = :imdbId")
    fun updateMovie(title: String, year: String, type: String, poster: String, imdbId: String)

    @Query("UPDATE ratings SET source = :source, value = :value" )
    fun updateRating(source: String, value: String)

    @Query("UPDATE movie_detail SET rated = :rated, runtime = :runtime, genre = :genre, released = :released, plot = :plot, director = :director, writer = :writer, actor = :actor, metascore = :metascore, imdbRating = :imdbRating, rating = :rating")
    fun updateMovieDetails(rated: String, runtime: String, genre: String, released: String, plot: String, director: String, writer: String, actor: String, metascore: String, imdbRating: String, rating: List<Rating>)

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :search || '%' ORDER BY year DESC LIMIT (:page * 30)")
    fun searchMovies(search: String, page: Int): LiveData<List<Movie>>

    @Query("SELECT * FROM movie_detail WHERE detailId = :detailId")
    fun getMovie(detailId: String): LiveData<MovieDetail>



}