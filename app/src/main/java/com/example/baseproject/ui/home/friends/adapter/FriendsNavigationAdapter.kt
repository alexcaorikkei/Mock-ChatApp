package com.example.baseproject.ui.home.friends.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.baseproject.ui.home.friends.ListFriendsFragment

class FriendsNavigationAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return ListFriendsFragment(position)
    }
}