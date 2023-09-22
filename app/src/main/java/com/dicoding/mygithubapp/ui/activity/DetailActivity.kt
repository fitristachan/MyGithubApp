package com.dicoding.mygithubapp.ui.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mygithubapp.R
import com.dicoding.mygithubapp.data.response.UserDetailResponse
import com.dicoding.mygithubapp.databinding.ActivityDetailBinding
import com.dicoding.mygithubapp.ui.activity.MainActivity.Companion.loadImage
import com.dicoding.mygithubapp.ui.adapter.SectionsPagerAdapter
import com.dicoding.mygithubapp.ui.viewmodel.*
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val favoriteUsersViewModel by viewModels<FavoriteUsersViewModel> {
        FavoriteUsersViewModelFactory.getInstance(application)
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = ""
        supportActionBar?.setElevation(0F)

        val typedValue = TypedValue()
        theme.resolveAttribute(
            com.google.android.material.R.attr.colorPrimarySurface,
            typedValue,
            true
        )
        supportActionBar?.setBackgroundDrawable(ColorDrawable(typedValue.data))

        val detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java
        )

        detailViewModel.userdetailResponse.observe(this) { userdetailResponse ->
            setUserDetailResponseData(userdetailResponse)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val username = intent.getStringExtra("username")
        detailViewModel.usersDetail(username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()


        binding.btnShare.setOnClickListener {
            shareContent("https://www.github.com/$username")
        }


        favoriteUsersViewModel.getFavoriteUsersByUsername(username).observe(this) { favoriteUsers ->
            if (favoriteUsers.isEmpty()) {
                binding.btnAddFav.visibility = View.VISIBLE
                binding.btnFaved.visibility = View.GONE
            } else {
                binding.btnAddFav.visibility = View.GONE
                binding.btnFaved.visibility = View.VISIBLE
            }
        }


        binding.btnAddFav.setOnClickListener {
            favoriteUsersViewModel.insertFavoriteUsers(username)
        }

        binding.btnFaved.setOnClickListener {
            favoriteUsersViewModel.deleteFavoriteUsers(username)
        }

    }

    private fun shareContent(content: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, content)

        val chooser = Intent.createChooser(intent, "Share via")
        startActivity(chooser)
    }


    private fun setUserDetailResponseData(userdetailResponse: UserDetailResponse) {
        if (userdetailResponse.avatarUrl.isNullOrEmpty()) {
            binding.imgItemAvatar.setBackgroundResource(R.drawable.person)
        } else {
            binding.imgItemAvatar.loadImage(userdetailResponse.avatarUrl)
        }

        if (userdetailResponse.name.isNullOrEmpty()) {
            binding.tvItemFullname.text = "No Name"
        } else {
            binding.tvItemFullname.text = userdetailResponse.name
        }

        binding.tvItemUsername.text = "@" + userdetailResponse.login

        if (userdetailResponse.location.isNullOrEmpty()) {
            binding.tvItemLocation.visibility = View.GONE
        } else {
            binding.tvItemLocation.visibility = View.VISIBLE
            binding.tvItemLocation.text = userdetailResponse.location
        }

        if (userdetailResponse.followers.toString().isNullOrEmpty()) {
            binding.tvItemFollowers.text = "0"
        } else {
            binding.tvItemFollowers.text = userdetailResponse.followers.toString()
        }

        if (userdetailResponse.following.toString().isNullOrEmpty()) {
            binding.tvItemFollowing.text = "0"
        } else {
            binding.tvItemFollowing.text = userdetailResponse.following.toString()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }


}