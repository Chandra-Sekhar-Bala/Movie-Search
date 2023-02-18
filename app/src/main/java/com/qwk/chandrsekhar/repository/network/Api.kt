package com.qwk.chandrsekhar.repository.network

import com.qwk.chandrsekhar.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

private val API_KEY = "85196c13c23d5dec643188c613ad99d9"

interface Api {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page : Int,
        @Query("api_key") api_key: String = API_KEY
    ): MovieResponse

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query : String,
        @Query("page") page : Int,
        @Query("api_key") api: String = API_KEY,
    ) : MovieResponse
}
