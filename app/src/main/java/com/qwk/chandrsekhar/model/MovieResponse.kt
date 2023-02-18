package com.qwk.chandrsekhar.model

import android.provider.MediaStore.Audio.Genres
import com.squareup.moshi.Json

data class MovieResponse(val page: Int, val results: List<Movie>) {
    data class Movie(
        val id: Int,
        val title: String,
        val genre_ids: List<Int>,
        @Json(name = "vote_average") val rating: Double,
        @Json(name = "poster_path") val posterUrl: String?
    )
}
