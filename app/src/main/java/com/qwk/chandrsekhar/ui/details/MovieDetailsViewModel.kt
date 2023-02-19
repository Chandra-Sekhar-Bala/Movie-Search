package com.qwk.chandrsekhar.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwk.chandrsekhar.model.MovieDetails
import com.qwk.chandrsekhar.repository.network.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailsViewModel : ViewModel() {

    private var _movieDetails = MutableLiveData<MovieDetails?>()
    val movieDetails: LiveData<MovieDetails?>
        get() = _movieDetails
    private val retrofit = MovieService.movieAPi


    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            val data = getMovieDetailsFromApi(movieId)
            if (data != null) {
                _movieDetails.value = data
            }else{
            }
        }
    }

    private suspend fun getMovieDetailsFromApi(movieId: Int): MovieDetails? {
        return withContext(Dispatchers.IO) {
            var data: MovieDetails? = null
            try {
                data = retrofit.getMovieDetils(movieID = movieId)
            } catch (_: java.lang.Exception) {
            }
            return@withContext data
        }
    }


}