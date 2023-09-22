package com.dicoding.mygithubapp.di

import android.content.Context
import com.dicoding.mygithubapp.data.local.room.FavoriteUsersDatabase
import com.dicoding.mygithubapp.data.repository.FavoriteUsersRepository
import com.dicoding.mygithubapp.data.retrofit.ApiConfig
import com.dicoding.mygithubapp.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavoriteUsersRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteUsersDatabase.getInstance(context)
        val dao = database.favoriteUsersDao()
        val appExecutors = AppExecutors()
        return FavoriteUsersRepository.getInstance(apiService, dao, appExecutors)
    }
}