package com.dicoding.mygithubapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mygithubapp.data.response.FollowersResponseItem
import com.dicoding.mygithubapp.databinding.ItemFollowListsBinding
import com.dicoding.mygithubapp.ui.activity.DetailActivity
import com.dicoding.mygithubapp.ui.activity.MainActivity.Companion.loadImage

class FollowersAdapter : ListAdapter<FollowersResponseItem, FollowersAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            ItemFollowListsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val followers = getItem(position)
        holder.bind(followers)
        val username = followers.login
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("username", username)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder(private val binding: ItemFollowListsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(followers: FollowersResponseItem) {
            binding.tvItemUsername.text = followers.login
            binding.imgItemAvatar.loadImage(followers.avatarUrl)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowersResponseItem>() {
            override fun areItemsTheSame(
                oldItem: FollowersResponseItem,
                newItem: FollowersResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FollowersResponseItem,
                newItem: FollowersResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }

    }
}