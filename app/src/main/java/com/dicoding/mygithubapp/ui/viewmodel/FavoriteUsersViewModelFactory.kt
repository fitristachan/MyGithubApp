package com.dicoding.mygithubapp.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mygithubapp.data.repository.FavoriteUsersRepository
import com.dicoding.mygithubapp.di.Injection

class FavoriteUsersViewModelFactory private constructor(private val favoriteUsersRepository: FavoriteUsersRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((FavoriteUsersViewModel::class.java))) {
            return FavoriteUsersViewModel(favoriteUsersRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: FavoriteUsersViewModelFactory? = null
        fun getInstance(context: Context): FavoriteUsersViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: FavoriteUsersViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}
