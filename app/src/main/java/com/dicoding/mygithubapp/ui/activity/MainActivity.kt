package com.dicoding.mygithubapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.dicoding.mygithubapp.R
import com.dicoding.mygithubapp.data.response.ItemsItem
import com.dicoding.mygithubapp.data.response.SearchResponse
import com.dicoding.mygithubapp.databinding.ActivityMainBinding
import com.dicoding.mygithubapp.ui.adapter.UsersListAdapter
import com.dicoding.mygithubapp.ui.viewmodel.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingsViewModel = ViewModelProvider(this, SettingsViewModelFactory(pref)).get(
            SettingsViewModel::class.java
        )

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsersList.layoutManager = layoutManager

        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        mainViewModel.usersList.observe(this) { users ->
            setUserData(users)
        }

        mainViewModel.searchResponse.observe(this) { searchResponse ->
            setSearchResponseData(searchResponse)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }


        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        searchBar.text = searchView.text
                        username = searchBar.text.toString()

                        if (username.isNullOrEmpty()) {
                            binding.rvUsersList.visibility = View.GONE
                            binding.layoutAttention.visibility = View.VISIBLE
                            binding.imgAttention.setBackgroundResource(R.drawable.person_search)
                            binding.tvAttention.setText("Type the name above and find your friends!")
                        } else {
                            binding.rvUsersList.visibility = View.VISIBLE
                            binding.layoutAttention.visibility = View.GONE
                            binding.tvShowResult.visibility = View.VISIBLE
                            mainViewModel.findUsers(username)
                        }
                        searchView.hide()
                        return@setOnEditorActionListener true
                    }
                    false
                }
        }
        binding.btnFavList.setOnClickListener() {
            val intentFavList = Intent(this, FavListActivity::class.java)
            startActivity(intentFavList)
        }
        binding.btnSettings.setOnClickListener() {
            val intentSettings = Intent(this, SettingsActivity::class.java)
            startActivity(intentSettings)
        }
    }

    private fun setUserData(users: List<ItemsItem>) {
        val adapter = UsersListAdapter()
        adapter.submitList(users)
        binding.rvUsersList.adapter = adapter
    }

    private fun setSearchResponseData(searchResponse: SearchResponse) {
        if (searchResponse.totalCount == 0) {
            binding.layoutAttention.visibility = View.VISIBLE
            binding.tvShowResult.visibility = View.GONE
            binding.imgAttention.setBackgroundResource(R.drawable.person_off)
            binding.tvAttention.setText(R.string.notExist)
        } else {
            binding.tvShowResult.text =
                resources.getString(R.string.showingResult, searchResponse.totalCount)
        }


    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        fun ImageView.loadImage(url: String?) {
            Glide.with(this.context)
                .load(url)
                .transform(CircleCrop())
                .into(this)
        }
    }
}