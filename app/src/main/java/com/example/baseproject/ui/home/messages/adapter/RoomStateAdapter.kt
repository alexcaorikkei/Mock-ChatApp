package com.example.baseproject.ui.home.messages.adapter

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.baseproject.domain.model.Response
import com.example.baseproject.ui.home.messages.RoomsFragment
import com.example.baseproject.ui.home.messages.model.RoomModel

class RoomStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return RoomsFragment(position)
    }
}