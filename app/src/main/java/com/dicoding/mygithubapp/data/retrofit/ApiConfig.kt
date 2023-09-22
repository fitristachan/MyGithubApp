package com.dicoding.mygithubapp.data.retrofit

import com.dicoding.mygithubapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val mySuperSecretKey = BuildConfig.KEY
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", mySuperSecretKey)
                    .build()
                chain.proceed(requestHeaders)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            val myGithubUrl = BuildConfig.BASE_URL
            val retrofit = Retrofit.Builder()
                .baseUrl(myGithubUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}