package com.qwk.chandrsekhar.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qwk.chandrsekhar.repository.database.MovieDAO

class MovieDetailsViewModelFactory(private val dao: MovieDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)){
            return MovieDetailsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}