package com.example.searchmovielocalcache.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.searchmovielocalcache.models.Movie

@Database(entities = (arrayOf(Movie::class)), version = 1)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "movies_db"

        private var instance: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }
            return instance
        }
    }

    abstract fun getMovieDao(): MovieDao
}