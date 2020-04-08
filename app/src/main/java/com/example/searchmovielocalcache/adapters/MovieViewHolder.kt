package com.example.searchmovielocalcache.adapters

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.searchmovielocalcache.R

class MovieViewHolder(itemView: View, private val onMovieListener: OnMovieListener):
    RecyclerView.ViewHolder(itemView),View.OnClickListener {

    val image: AppCompatImageView = itemView.findViewById(R.id.movie_image)
    val title: TextView = itemView.findViewById(R.id.movie_title)
    val year: TextView = itemView.findViewById(R.id.movie_year)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onMovieListener.onMovieClick(adapterPosition)
    }
}