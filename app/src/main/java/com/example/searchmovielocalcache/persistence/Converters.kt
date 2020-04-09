package com.example.searchmovielocalcache.persistence

import androidx.room.TypeConverter
import com.example.searchmovielocalcache.models.Rating
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object Converters{

    @TypeConverter
    @JvmStatic
    fun fromListToRating(list: List<Rating>): String{
        val gson: Gson = Gson()
        val type: Type = object: TypeToken<List<Rating>>(){}.type

        val json: String = gson.toJson(list, type)
        return json

    }

    @TypeConverter
    @JvmStatic
    fun fromRatingToList(ratingString: String): List<Rating> {
        val gson: Gson = Gson()
        val type: Type = object: TypeToken<List<Rating>>(){}.type
        val ratingList: List<Rating> = gson.fromJson(ratingString, type)
        return ratingList
    }
}

