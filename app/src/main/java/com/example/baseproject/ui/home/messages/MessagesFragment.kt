package com.example.baseproject.ui.home.messages

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentMessagesBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.messages.adapter.MessageAdapter
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment : BaseFragment<FragmentMessagesBinding, MessagesViewModel>(R.layout.fragment_messages) {
    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: MessagesViewModel by viewModels()
    override fun getVM() = viewModel

    override fun bindingStateView() {
        super.bindingStateView()
        binding.rvMessages.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.rvMessages.adapter = MessageAdapter(listOf(
            MessageModel(1, "Nguyen Van A", "Hello", "10:00", "", false),
            MessageModel(2, "Nguyen Van B", "Hello", "10:00", "", true),
            MessageModel(3, "Nguyen Van C", "Hello", "10:00", "", true),
            MessageModel(4, "Nguyen Van D", "Hello", "10:00", "", false),
            MessageModel(5, "Nguyen Van E", "Hello", "10:00", "", false),
            MessageModel(6, "Nguyen Van F", "Hello", "10:00", "", true),
            MessageModel(7, "Nguyen Van G", "Hello", "10:00", "", false),
            MessageModel(8, "Nguyen Van H", "Hello", "10:00", "", true),
            MessageModel(9, "Nguyen Van I", "Hello", "10:00", "", false),
            MessageModel(10, "Nguyen Van J", "Hello", "10:00", "", true),
            MessageModel(11, "Nguyen Van K", "Hello", "10:00", "", false),
            MessageModel(12, "Nguyen Van L", "Hello", "10:00", "", true),
            MessageModel(7, "Nguyen Van G", "Hello", "10:00", "", false),
            MessageModel(8, "Nguyen Van H", "Hello", "10:00", "", true),
            MessageModel(9, "Nguyen Van I", "Hello", "10:00", "", false),
            MessageModel(10, "Nguyen Van J", "Hello", "10:00", "", true),
            MessageModel(11, "Nguyen Van K", "Hello", "10:00", "", false),
            MessageModel(12, "Nguyen Van L", "Hello", "10:00", "", true),
        ))
    }
}