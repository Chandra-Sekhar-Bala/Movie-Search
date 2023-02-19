package com.qwk.chandrsekhar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.qwk.chandrsekhar.Constants
import com.qwk.chandrsekhar.R
import com.qwk.chandrsekhar.model.MovieResponse

class MovieAdapter(private val context: Context, private val listener: MovieClickListener?) :
    ListAdapter<MovieResponse.Movie, MovieAdapter.MyViewHolder>(DiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.title.text = data.title
        holder.rating.text = context.getString(R.string.imdb_rating, data.rating.toString())

        // i need mox 3 genre : if they exist then show up else catch
        try {
            holder.tag1.text = getGenreName(data.genre_ids[0])
            holder.tag1.visibility = View.VISIBLE
            holder.tag2.text = getGenreName(data.genre_ids[1])
            holder.tag2.visibility = View.VISIBLE
            holder.tag3.text = getGenreName(data.genre_ids[2])
            holder.tag3.visibility = View.VISIBLE
        } catch (_: java.lang.Exception) {
        }

        Glide.with(context)
            .load(Constants.posterPrefix + data.posterUrl)
            .fitCenter()
            .transform(RoundedCorners(60))
            .placeholder(R.drawable.movie_icon)
            .into(holder.posterImg)

        // Listening to movie item click
        holder.itemView.setOnClickListener {
            if (listener !== null) {
                listener.onMovieItemCLickListener(data.id)
            }
        }
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        val title = itemView.findViewById(R.id.movie_title) as TextView
        val rating = itemView.findViewById(R.id.movie_rating) as TextView
        val tag1 = itemView.findViewById(R.id.tag1_movie) as TextView
        val tag2 = itemView.findViewById(R.id.tag2_movie) as TextView
        val tag3 = itemView.findViewById(R.id.tag3_movie) as TextView
        val posterImg = itemView.findViewById(R.id.movie_poster_img) as ImageView
    }

    object DiffCallBack : DiffUtil.ItemCallback<MovieResponse.Movie>() {
        override fun areItemsTheSame(
            oldItem: MovieResponse.Movie,
            newItem: MovieResponse.Movie
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MovieResponse.Movie,
            newItem: MovieResponse.Movie
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private fun getGenreName(genreId: Int): String {
        return Constants.GENRE_MAP[genreId] ?: "Unknown"
    }
}

interface MovieClickListener {
    fun onMovieItemCLickListener(movieId: Int)
}