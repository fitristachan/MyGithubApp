package com.dicoding.mygithubapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubapp.data.response.FollowersResponseItem
import com.dicoding.mygithubapp.data.response.FollowingResponseItem
import com.dicoding.mygithubapp.databinding.FragmentFollBinding
import com.dicoding.mygithubapp.ui.adapter.FollowersAdapter
import com.dicoding.mygithubapp.ui.adapter.FollowingAdapter
import com.dicoding.mygithubapp.ui.viewmodel.FollowersModel
import com.dicoding.mygithubapp.ui.viewmodel.FollowingModel

class FollFragment : Fragment() {
    private lateinit var binding: FragmentFollBinding
    private var position: Int = 0
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentFollBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollLists.layoutManager = layoutManager

        val followersViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowersModel::class.java]
        followersViewModel.followersResponse.observe(viewLifecycleOwner) { followers ->
            setFollowersData(followers)
        }

        followersViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        val followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowingModel::class.java]
        followingViewModel.followingResponse.observe(viewLifecycleOwner) { following ->
            setFollowingData(following)
        }

        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }


        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        if (position == 1) {
            if (followingViewModel.followingResponse.toString().isEmpty()) {
                binding.tvEmptyFollow.visibility = View.VISIBLE
                binding.tvEmptyFollow.text = "$username doesn't have any following"
            } else {
                binding.rvFollLists.visibility = View.VISIBLE
                followingViewModel.getFollowing(username)
            }
        } else {
            if (followersViewModel.followersResponse.toString().isEmpty()) {
                binding.tvEmptyFollow.visibility = View.VISIBLE
                binding.tvEmptyFollow.text = "$username doesn't have any followers"
            } else {
                binding.rvFollLists.visibility = View.VISIBLE
                followersViewModel.getFollowers(username)
            }
        }
    }


    private fun setFollowersData(followers: List<FollowersResponseItem>) {
        val adapter = FollowersAdapter()
        adapter.submitList(followers)
        binding.rvFollLists.adapter = adapter
    }

    private fun setFollowingData(following: List<FollowingResponseItem>) {
        val adapter = FollowingAdapter()
        adapter.submitList(following)
        binding.rvFollLists.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_USERNAME = "param2"
        const val ARG_POSITION = "param1"

        fun newInstance(position: Int, username: String?): FollFragment {
            val fragment = FollFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }
}
