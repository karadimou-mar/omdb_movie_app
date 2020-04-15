package com.example.searchmovielocalcache.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.searchmovielocalcache.models.Movie
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
    fun insertMovieDetails(movie: Movie)

    // Custom update queries so timestamp don't get removed
    @Query("UPDATE movies SET title = :title, year = :year, type = :type, poster = :poster, plot = :plot WHERE imdbID = :imdbID")
    fun updateMovie( title: String, year: String, type: String, poster: String, imdbID: String, plot: String)

//    @Query("UPDATE ratings SET source = :source, value = :value")
//    fun updateRating(source: String, value: String)

    @Query("UPDATE movies SET rated = :rated, runtime = :runtime, genre = :genre, released = :released, plot = :plot, director = :director, writer = :writer, actor = :actor, metascore = :metascore, rating = :rating, imdbRating =:imdbRating, title= :title WHERE imdbID= :id")
    fun updateMovieDetails(title:String,  rated: String, runtime: String, genre: String, released: String, plot: String, director: String,
                           writer: String, actor: String, metascore: String, rating: List<Rating>, imdbRating: String, id: String)

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :search || '%' ORDER BY imdbrating DESC LIMIT (:page * 10)")
    fun searchMovies(search: String, page: Int): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE imdbID = :id")
    fun getMovieByID(id: String): LiveData<Movie>

    @Query("UPDATE movies SET timestamp = :timestamp WHERE imdbID = :imdbID")
    fun updateMovieTimestamp(timestamp: Int, imdbID: String)
}
