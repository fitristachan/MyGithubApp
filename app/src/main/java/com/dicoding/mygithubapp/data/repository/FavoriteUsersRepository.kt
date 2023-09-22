package com.dicoding.mygithubapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.mygithubapp.data.local.entity.FavoriteUsersEntity
import com.dicoding.mygithubapp.data.local.room.FavoriteUsersDao
import com.dicoding.mygithubapp.data.response.UserDetailResponse
import com.dicoding.mygithubapp.data.retrofit.ApiService
import com.dicoding.mygithubapp.data.Result
import com.dicoding.mygithubapp.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteUsersRepository private constructor(
    private val apiService: ApiService,
    private val favoriteUsersDao: FavoriteUsersDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<FavoriteUsersEntity>>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun insertFavoriteUsers(username: String?): LiveData<Result<List<FavoriteUsersEntity>>> {
        result.value = Result.Loading
        val client = apiService.getUserDetail(username!!)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val username = response.body()?.login
                    val avatarUrl = response.body()?.avatarUrl
                    val favoriteList = ArrayList<FavoriteUsersEntity>()
                    val favoriteUser = FavoriteUsersEntity(
                        username!!,
                        avatarUrl
                    )
                    favoriteList.add(favoriteUser)
                    appExecutors.diskIO.execute {
                        favoriteUsersDao.deleteFavoriteUsers(username)
                        favoriteUsersDao.insert(favoriteList)
                    }
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        appExecutors.diskIO.execute {
            val updatedList = favoriteUsersDao.getAllFavoriteUsers()

            result.addSource(updatedList) { newData: List<FavoriteUsersEntity> ->
                result.value = Result.Success(newData)
            }
        }
        return result
    }

    fun deleteFavoriteUsers(username: String?) {
        appExecutors.diskIO.execute {
            favoriteUsersDao.deleteFavoriteUsers(username!!)
        }
    }

    fun getFavoriteUsersList(): LiveData<List<FavoriteUsersEntity>> {
        return favoriteUsersDao.getAllFavoriteUsers()
    }

    fun getFavoriteUsersByUsername(username: String?): LiveData<List<FavoriteUsersEntity>> {
        if (username != null) {
            return favoriteUsersDao.getFavoriteUserByUsername(username)
        } else {
            return MutableLiveData<List<FavoriteUsersEntity>>().apply { value = emptyList() }
        }
    }


    companion object {
        @Volatile
        private var instance: FavoriteUsersRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteUsersDao: FavoriteUsersDao,
            appExecutors: AppExecutors
        ): FavoriteUsersRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteUsersRepository(apiService, favoriteUsersDao, appExecutors)
            }.also { instance = it }
    }
}