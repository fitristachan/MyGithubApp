package com.dicoding.mygithubapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mygithubapp.data.response.FollowingResponseItem
import com.dicoding.mygithubapp.databinding.ItemFollowListsBinding
import com.dicoding.mygithubapp.ui.activity.DetailActivity
import com.dicoding.mygithubapp.ui.activity.MainActivity.Companion.loadImage

class FollowingAdapter : ListAdapter<FollowingResponseItem, FollowingAdapter.MyViewHolder>(
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
        val following = getItem(position)
        holder.bind(following)
        val username = following.login
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("username", username)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder(private val binding: ItemFollowListsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(following: FollowingResponseItem) {
            binding.tvItemUsername.text = following.login
            binding.imgItemAvatar.loadImage(following.avatarUrl)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowingResponseItem>() {
            override fun areItemsTheSame(
                oldItem: FollowingResponseItem,
                newItem: FollowingResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FollowingResponseItem,
                newItem: FollowingResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}