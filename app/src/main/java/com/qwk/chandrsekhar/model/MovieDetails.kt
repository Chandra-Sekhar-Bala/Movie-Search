package com.qwk.chandrsekhar.model
import com.squareup.moshi.Json
import java.util.zip.DeflaterOutputStream

data class MovieDetails(
    val id : Int,
    val title: String,
    @Json(name = "overview") val about: String,
    @Json(name = "poster_path") val poster_img: String,
    val status: String,
    val release_date: String,
    val revenue: Int,
    @Json(name = "vote_average") val rating: Double,
    val popularity: Double,
    val genres: List<Genre>,
    val tagline : String
)

data class Genre(
    val id: Int,
    val name: String
)
