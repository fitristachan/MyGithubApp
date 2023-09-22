package com.dicoding.mygithubapp.data.retrofit

import com.dicoding.mygithubapp.data.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun findUserByUsername(
        @Query("q") username: String?
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<FollowersResponseItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<FollowingResponseItem>>
}
