package com.dicoding.mygithubapp.ui.viewmodel

import com.dicoding.mygithubapp.data.repository.FavoriteUsersRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class FavoriteUsersViewModelTest {
    private lateinit var favoriteUsersViewModel: FavoriteUsersViewModel
    private lateinit var favoriteUsersRepository: FavoriteUsersRepository
    private val dummyUsername = "fitristachan"

    @Before
    fun before() {
        favoriteUsersRepository = mock(FavoriteUsersRepository::class.java)
        favoriteUsersViewModel = FavoriteUsersViewModel(favoriteUsersRepository)
    }
    @Test
    fun testInsertFavoriteUsers() {
        favoriteUsersViewModel.insertFavoriteUsers(dummyUsername)
        verify(favoriteUsersRepository).insertFavoriteUsers(dummyUsername)
    }


    @Test
    fun testGetFavoriteUsersByUsername() {
        favoriteUsersViewModel.getFavoriteUsersByUsername(dummyUsername)
        verify(favoriteUsersRepository).getFavoriteUsersByUsername(dummyUsername)
    }

    @Test
    fun testGetFavoriteUsersList() {
        favoriteUsersViewModel.getFavoriteUsersList()
        verify(favoriteUsersRepository).getFavoriteUsersList()
    }

    @Test
    fun testDeleteFavoriteUsers() {
        favoriteUsersViewModel.deleteFavoriteUsers(dummyUsername)
        verify(favoriteUsersRepository).deleteFavoriteUsers(dummyUsername)
    }

}