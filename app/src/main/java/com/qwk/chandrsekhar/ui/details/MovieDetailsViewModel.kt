package com.qwk.chandrsekhar.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwk.chandrsekhar.model.MovieDetails
import com.qwk.chandrsekhar.repository.database.MovieDAO
import com.qwk.chandrsekhar.repository.database.MovieEntity
import com.qwk.chandrsekhar.repository.network.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.KeyStore.TrustedCertificateEntry

class MovieDetailsViewModel(private val dao: MovieDAO) : ViewModel() {

    private var _movieDetails = MutableLiveData<MovieDetails?>()
    val movieDetails: LiveData<MovieDetails?>
        get() = _movieDetails
    private val retrofit = MovieService.movieAPi
    var ifMovieSaved = MutableLiveData<Boolean>()
    private fun ifThisMovieSaved(id: Int) {
        viewModelScope.launch {
            ifMovieSaved.value = ifExist(id)
        }
    }

    private suspend fun ifExist(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext dao.getAllMovies().any { it.id == id }
        }
    }

    fun getMovieDetails(movieId: Int) {
        // first check is saved on not
        ifThisMovieSaved(movieId)

        viewModelScope.launch {
            val data = getMovieDetailsFromApi(movieId)
            if (data != null) {
                _movieDetails.value = data
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

    // if the item is saved then delete else save
    fun saveMovie() {
        val item = _movieDetails.value!!
        // if the movie is not saved then save else delete
        if (ifMovieSaved.value == false) {
            val movie = MovieEntity(
                item.id,
                item.title,
                item.genres.map { it.id },
                item.rating,
                item.poster_img
            )
            viewModelScope.launch {
                dao.insertMovie(movie)
            }
            ifMovieSaved.value = true
        } else {
            viewModelScope.launch {
                dao.deleteMovie(item.id)
            }
            ifMovieSaved.value = false
        }
    }


}