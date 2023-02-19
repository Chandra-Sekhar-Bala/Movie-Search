package com.qwk.chandrsekhar.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qwk.chandrsekhar.repository.database.MovieDAO

class FavoriteViewModelFactory(private val dao: MovieDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown Class")
    }
}