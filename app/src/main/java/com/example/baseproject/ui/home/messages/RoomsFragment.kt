package com.example.baseproject.ui.home.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentRoomsBinding
import com.example.baseproject.databinding.ItemRoomBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.extension.KEY_ID_RECEIVER
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.messages.adapter.OnRoomClickListener
import com.example.baseproject.ui.home.messages.adapter.RoomAdapter
import com.example.baseproject.ui.home.messages.model.RoomModel
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoomsFragment(private val position: Int) : BaseFragment<FragmentRoomsBinding, MessagesViewModel>(R.layout.fragment_rooms), OnRoomClickListener {
    constructor() : this(0)

    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: MessagesViewModel by viewModels({requireParentFragment()})
    override fun getVM() = viewModel

    private var listRoomChat = listOf<RoomModel>()

    private val roomAdapter = RoomAdapter(this)

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.swipeRefreshLayout.isEnabled = false
        binding.rvMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMessages.adapter = roomAdapter
        roomAdapter.submitList(listRoomChat)
    }

    override fun bindingStateView() {
        super.bindingStateView()
        val responseLiveData = when(position) {
            0 -> viewModel.roomResponse
            else -> viewModel.searchResponse
        }
        responseLiveData.observe(viewLifecycleOwner) { listRoom ->
            when(listRoom) {
                is Response.Failure -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is Response.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Response.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    listRoomChat = listRoom.data
                    roomAdapter.submitList(listRoomChat)
                }
            }
        }
    }

    override fun onMessageClicked(position: Int, view: ItemRoomBinding) {
        val bundle = Bundle()
        bundle.putString(KEY_ID_RECEIVER, listRoomChat[position].friendId)
        appNavigation.openHomeToChatScreen(bundle)
    }
}