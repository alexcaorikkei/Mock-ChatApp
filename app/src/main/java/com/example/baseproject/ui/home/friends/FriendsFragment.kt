package com.example.baseproject.ui.home.friends


import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentFriendsBinding
import com.example.baseproject.domain.model.FriendModel
import com.example.baseproject.domain.model.Response
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.friends.adapter.FriendsNavigationAdapter
import com.example.core.base.fragment.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@AndroidEntryPoint
class FriendsFragment : BaseFragment<FragmentFriendsBinding, FriendsViewModel>(R.layout.fragment_friends) {
    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: FriendsViewModel by viewModels()
    override fun getVM() = viewModel


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.swipeRefreshLayout.isEnabled = false
    }

    override fun bindingStateView() {
        super.bindingStateView()
        binding.friendsViewPager.adapter = FriendsNavigationAdapter(this)
        TabLayoutMediator(binding.friendsNav, binding.friendsViewPager) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.friends)
                1 -> getString(R.string.all)
                else -> getString(R.string.request)
            }
        }.attach()

        viewModel.listFriendLiveData.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Failure -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is Response.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Response.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }

        viewModel.friendStateResponse.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Failure -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is Response.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Response.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }



    override fun setOnClick() {
        var timer = Timer()
        binding.apply {
            etSearch.addTextChangedListener {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        viewModel.searchFriend(it.toString())
                    }
                }, 500)
            }
        }
    }
}

