package com.qwk.chandrsekhar.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwk.chandrsekhar.model.MovieResponse
import com.qwk.chandrsekhar.repository.network.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class Progress {LOADING, SUCCESSFUL, FAILED}
class HomeViewModel : ViewModel() {
    private var _movieData = MutableLiveData<List<MovieResponse.Movie>?>()
    val movieData: LiveData<List<MovieResponse.Movie>?>
        get() = _movieData
    private val retrofit = MovieService.movieAPi
    private var page = 1

    private var _progress = MutableLiveData<Progress>()
    val progress : LiveData<Progress>
    get() = _progress


    init {
        getPopularMovies()
    }

    private fun getPopularMovies() {
        viewModelScope.launch {
            _progress.value = Progress.LOADING
            val data = _getPopularMovies()
            if (data != null) {
                _progress.value = Progress.SUCCESSFUL
                if (page > 1) {
                    _movieData.value = _movieData.value!! + data
                } else {
                    _movieData.value = data
                }
                page++

            }else{
                _progress.value = Progress.FAILED
            }
        }
    }

    private suspend fun _getPopularMovies(): List<MovieResponse.Movie>? {
        return withContext(Dispatchers.IO) {
            var data: List<MovieResponse.Movie>? = null
            try {
                data = retrofit.getPopularMovies(page = page).results
            } catch (_: java.lang.Exception) { }
            return@withContext data
        }
    }

    fun loadNextPage() {
        getPopularMovies()
    }
}