package com.example.baseproject.ui.home.messages

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentMessagesBinding
import com.example.baseproject.databinding.ItemRoomBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.extension.KEY_ID_RECEIVER
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.messages.adapter.RoomAdapter
import com.example.baseproject.ui.home.messages.adapter.OnRoomClickListener
import com.example.baseproject.ui.home.messages.model.RoomModel
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment : BaseFragment<FragmentMessagesBinding, MessagesViewModel>(R.layout.fragment_messages), OnRoomClickListener {
    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: MessagesViewModel by viewModels()
    override fun getVM() = viewModel
    private var listRoomChat = listOf<RoomModel>()

    override fun bindingStateView() {
        super.bindingStateView()
        binding.swipeRefreshLayout.isEnabled = false
        binding.rvMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMessages.adapter = RoomAdapter(listOf(), this)
        viewModel.searchResponse.observe(viewLifecycleOwner) { listRoom ->
            when(listRoom) {
                is Response.Failure -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is Response.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Response.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.rvMessages.adapter = RoomAdapter(listRoom.data, this)
                    listRoomChat = listRoom.data
                }
            }
        }
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.tvTitle.setOnClickListener {
            appNavigation.openHomeToChatScreen()
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.searchRoom("")
        }
    }

    override fun onMessageClicked(position: Int, view: ItemRoomBinding) {
        val bundle = Bundle()
        bundle.putString(KEY_ID_RECEIVER, listRoomChat[position].friendId)
        appNavigation.openHomeToChatScreen(bundle)
    }
}