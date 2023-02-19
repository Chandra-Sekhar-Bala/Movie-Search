package com.qwk.chandrsekhar.model
import com.squareup.moshi.Json

data class MovieDetails(
    val title: String,
    @Json(name = "overview") val about: String,
    @Json(name = "poster_path") val poster_img: String,
    val status: String,
    val release_date: String,
    val revenue: Int,
    @Json(name = "vote_average") val rating: String,
    val popularity: Double,
    val genres: List<Genre>
)

data class Genre(
    val name: String
)
