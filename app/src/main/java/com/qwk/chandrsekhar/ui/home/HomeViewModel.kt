package com.qwk.chandrsekhar.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwk.chandrsekhar.model.MovieResponse
import com.qwk.chandrsekhar.repository.network.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class Progress { LOADING, SUCCESSFUL, FAILED }
class HomeViewModel : ViewModel() {
    private var _popularMovieData = MutableLiveData<List<MovieResponse.Movie>?>()
    val popularMovieData: LiveData<List<MovieResponse.Movie>?>
        get() = _popularMovieData
    private val retrofit = MovieService.movieAPi
    private var popularPage = 1
    private var _popularMovieProgress = MutableLiveData<Progress>()
    val popularMovieProgress: LiveData<Progress>
        get() = _popularMovieProgress

    private var _searchMovieData = MutableLiveData<List<MovieResponse.Movie>?>()
    val searchMovieData: LiveData<List<MovieResponse.Movie>?>
        get() = _searchMovieData
    private var searchPage = 1
    private var _searchProgress = MutableLiveData<Progress>()
    val searchProgress: LiveData<Progress>
        get() = _searchProgress
    private var prevMovieRequest: String = ""
    private var searchJob: Job? = null

    init {
        getPopularMovies()
    }

    private fun getPopularMovies() {
        viewModelScope.launch {
            _popularMovieProgress.value = Progress.LOADING
            val data = _getPopularMovies()
            if (data != null) {
                _popularMovieProgress.value = Progress.SUCCESSFUL
                if (popularPage > 1) {
                    _popularMovieData.value = _popularMovieData.value!! + data
                } else {
                    _popularMovieData.value = data
                }
                popularPage++
            } else {
                _popularMovieProgress.value = Progress.FAILED
            }
        }
    }

    private suspend fun _getPopularMovies(): List<MovieResponse.Movie>? {
        return withContext(Dispatchers.IO) {
            var data: List<MovieResponse.Movie>? = null
            try {
                data = retrofit.getPopularMovies(page = popularPage).results
            } catch (_: java.lang.Exception) {
            }
            return@withContext data
        }
    }

    fun loadNexPopularMoviePage() {
        getPopularMovies()
    }

    // search for movie
    fun searchMovie(movieReq: String?) {
        if (movieReq == null) return
        // for new request page count wil start from 1
        if (prevMovieRequest != movieReq) searchPage = 1

        viewModelScope.launch {
            _searchProgress.value = Progress.LOADING
            val data = getSearchData(movieReq)
            if (data != null) {
                _searchProgress.value = Progress.SUCCESSFUL
                if (searchPage > 1) {
                    _searchMovieData.value = _searchMovieData.value!! + data
                } else {
                    _searchMovieData.value = data
                }
                searchPage++
                prevMovieRequest = movieReq
            } else {
                _searchProgress.value = Progress.FAILED
            }
        }
    }

    // Fetch query result
    private suspend fun getSearchData(movieReq: String): List<MovieResponse.Movie>? {
        searchJob?.cancel()
        searchJob = Job()

        return withContext(Dispatchers.IO + searchJob!!) {
            var data: List<MovieResponse.Movie>? = null
            try {
                data = retrofit.searchMovie(query = movieReq, page = searchPage).results
            } catch (_: java.lang.Exception) {
            }
            return@withContext data
        }
    }

    fun loadNexSearchMoviePage() {
        searchMovie(prevMovieRequest)
    }
}