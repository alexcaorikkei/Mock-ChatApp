package com.example.baseproject.ui.home.friends.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.baseproject.ui.home.friends.ListFriendsFragment
import com.example.baseproject.domain.model.FriendState

class FriendsNavigationAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val states = when(position) {
            0 -> listOf(FriendState.FRIEND)
            1 -> listOf(FriendState.NONE, FriendState.ADDED, FriendState.REQUEST, FriendState.FRIEND)
            else -> listOf(FriendState.REQUEST, FriendState.ADDED)
        }
        return ListFriendsFragment.newInstance(states)
    }
}