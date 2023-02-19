package com.qwk.chandrsekhar.ui.favorite

import android.util.Log
import androidx.lifecycle.*
import com.qwk.chandrsekhar.model.MovieDetails
import com.qwk.chandrsekhar.model.MovieResponse
import com.qwk.chandrsekhar.repository.database.MovieDAO
import com.qwk.chandrsekhar.repository.database.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(private val dao: MovieDAO) : ViewModel() {
    private var _movies = MutableLiveData<List<MovieResponse.Movie?>>()
    val movies: LiveData<List<MovieResponse.Movie?>>
        get() = _movies
    fun getAllMovies(){
        viewModelScope.launch {
            _movies.value = dao.getAllMovies().map { it.toMovie() }
        }
    }
    fun MovieEntity.toMovie(): MovieResponse.Movie {
        return MovieResponse.Movie(id, title, genres, rating, postUrl)
    }
    fun deleteFromDatabase(id: Int) {
        viewModelScope.launch {
            dao.deleteMovie(id)
            getAllMovies()
        }
    }
}