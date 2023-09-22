package com.dicoding.mygithubapp.ui.activity

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubapp.R
import com.dicoding.mygithubapp.data.response.ItemsItem
import com.dicoding.mygithubapp.databinding.ActivityFavListBinding
import com.dicoding.mygithubapp.ui.adapter.UsersListAdapter
import com.dicoding.mygithubapp.ui.viewmodel.FavoriteUsersViewModel
import com.dicoding.mygithubapp.ui.viewmodel.FavoriteUsersViewModelFactory


class FavListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavListBinding
    private val favoriteUsersViewModel by viewModels<FavoriteUsersViewModel> {
        FavoriteUsersViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_list)

        binding = ActivityFavListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "My Favorite"
        supportActionBar?.setElevation(0F)

        val typedValue = TypedValue()
        theme.resolveAttribute(
            com.google.android.material.R.attr.colorSecondaryVariant,
            typedValue,
            true
        )
        supportActionBar?.setBackgroundDrawable(ColorDrawable(typedValue.data))

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsersList.layoutManager = layoutManager

        favoriteUsersViewModel.getFavoriteUsersList().observe(this) { favoriteUsers ->
            if (favoriteUsers.isNullOrEmpty()) {
                binding.layoutAttention.visibility = View.VISIBLE
                binding.rvUsersList.visibility = View.GONE
                binding.imgAttention.setBackgroundResource(R.drawable.person_off)
                binding.tvAttention.setText("You don't have any favorite user")
            } else {
                binding.progressBar.visibility = View.GONE
                binding.layoutAttention.visibility = View.GONE
                binding.rvUsersList.visibility = View.VISIBLE
                val items = arrayListOf<ItemsItem>()
                favoriteUsers.map {
                    val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl!!)
                    items.add(item)
                }
                val adapter = UsersListAdapter()
                adapter.submitList(items)
                binding.rvUsersList.adapter = adapter
            }
        }

    }
}