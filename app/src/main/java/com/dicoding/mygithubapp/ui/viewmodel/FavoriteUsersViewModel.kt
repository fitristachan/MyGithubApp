package com.dicoding.mygithubapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.mygithubapp.data.repository.FavoriteUsersRepository

class FavoriteUsersViewModel(private val favoriteUsersRepository: FavoriteUsersRepository) :
    ViewModel() {
    fun insertFavoriteUsers(username: String?) =
        favoriteUsersRepository.insertFavoriteUsers(username)

    fun getFavoriteUsersList() = favoriteUsersRepository.getFavoriteUsersList()

    fun deleteFavoriteUsers(username: String?) {
        favoriteUsersRepository.deleteFavoriteUsers(username)
    }

    fun getFavoriteUsersByUsername(username: String?) =
        favoriteUsersRepository.getFavoriteUsersByUsername(username)
}