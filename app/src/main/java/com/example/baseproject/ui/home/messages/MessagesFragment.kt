package com.example.baseproject.ui.home.messages

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentMessagesBinding
import com.example.baseproject.databinding.ItemMessageBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.extension.KEY_ID_RECEIVER
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.friends.model.FriendItemModel
import com.example.baseproject.ui.home.messages.adapter.MessageAdapter
import com.example.baseproject.ui.home.messages.adapter.OnMessageClickListener
import com.example.baseproject.ui.home.messages.model.RoomModel
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment : BaseFragment<FragmentMessagesBinding, MessagesViewModel>(R.layout.fragment_messages), OnMessageClickListener {
    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: MessagesViewModel by viewModels()
    override fun getVM() = viewModel
    private var listRoomChat = listOf<RoomModel>()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
    }

    override fun bindingStateView() {
        super.bindingStateView()
        binding.rvMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMessages.adapter = MessageAdapter(listOf(), this)
        viewModel.searchResponse.observe(viewLifecycleOwner) { listMessages ->
            when(listMessages) {
                is Response.Failure -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is Response.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Response.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.rvMessages.adapter = MessageAdapter(listMessages.data, this)
                    listRoomChat = listMessages.data
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
            viewModel.searchMessages("")
        }
    }

    override fun onMessageClicked(position: Int, view: ItemMessageBinding) {
        val bundle = Bundle()
        bundle.putString(KEY_ID_RECEIVER, listRoomChat[position].friendId)
        appNavigation.openHomeToChatScreen(bundle)
    }
}