package com.dicoding.mygithubapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.mygithubapp.data.local.entity.FavoriteUsersEntity

@Dao
interface FavoriteUsersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUserEntity: List<FavoriteUsersEntity>)

    @Query("SELECT * FROM favoriteuser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<List<FavoriteUsersEntity>>

    @Query("DELETE FROM favoriteuser WHERE username = :username")
    fun deleteFavoriteUsers(username: String)

    @Query("SELECT * from favoriteuser")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUsersEntity>>

}