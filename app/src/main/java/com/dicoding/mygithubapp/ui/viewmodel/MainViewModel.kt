package com.dicoding.mygithubapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubapp.data.response.ItemsItem
import com.dicoding.mygithubapp.data.response.SearchResponse
import com.dicoding.mygithubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _searchResponse = MutableLiveData<SearchResponse>()
    val searchResponse: LiveData<SearchResponse> = _searchResponse

    private val _usersList = MutableLiveData<List<ItemsItem>>()
    val usersList: LiveData<List<ItemsItem>> = _usersList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainActivity"
    }


    fun findUsers(username: String?) {
        if (username.isNullOrEmpty()) {
            return
        }
        _isLoading.value = true
        val client = ApiConfig.getApiService().findUserByUsername(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _searchResponse.value = response.body()
                    _usersList.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}

