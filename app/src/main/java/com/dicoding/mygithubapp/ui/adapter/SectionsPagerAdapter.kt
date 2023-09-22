package com.dicoding.mygithubapp.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.mygithubapp.ui.fragment.FollFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String?) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollFragment.ARG_POSITION, position + 1)
            putString(FollFragment.ARG_USERNAME, username)
        }
        return fragment
    }
}