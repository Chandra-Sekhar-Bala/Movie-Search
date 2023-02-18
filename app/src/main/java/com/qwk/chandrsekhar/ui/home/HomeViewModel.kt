package com.qwk.chandrsekhar.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwk.chandrsekhar.model.MovieResponse
import com.qwk.chandrsekhar.repository.network.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private var _movieData = MutableLiveData<List<MovieResponse.Movie>>()
    val movieData : MutableLiveData<List<MovieResponse.Movie>>
    get() = _movieData
    private val retrofit = MovieService.movieAPi

    init {
        getPopularMovies()
    }

    fun getPopularMovies(){
        viewModelScope.launch {
            _movieData.value = _getPopularMovies()
        }
    }

    private suspend fun _getPopularMovies(): List<MovieResponse.Movie> {
       return  withContext(Dispatchers.IO) {
            return@withContext retrofit.getPopularMovies().results
        }
    }

}