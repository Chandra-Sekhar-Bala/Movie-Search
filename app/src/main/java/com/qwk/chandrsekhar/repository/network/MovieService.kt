package com.qwk.chandrsekhar.repository.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


val BASE_URL = "https://api.themoviedb.org/3/"

// Build the Moshi object that Retrofit will be using
val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//  Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

// the caller
object MovieService {
    val movieAPi : Api by lazy {
        retrofit.create(Api::class.java)
    }
}
