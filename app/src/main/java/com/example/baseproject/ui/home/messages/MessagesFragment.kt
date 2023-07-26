package com.example.baseproject.ui.home.messages

import android.os.Bundle
import android.view.View
import android.view.View.OnTouchListener
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentMessagesBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.messages.adapter.RoomStateAdapter
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@AndroidEntryPoint
class MessagesFragment : BaseFragment<FragmentMessagesBinding, MessagesViewModel>(R.layout.fragment_messages) {
    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: MessagesViewModel by viewModels()
    override fun getVM() = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.vpMessages.adapter = RoomStateAdapter(this)
        binding.vpMessages.isUserInputEnabled = false
    }

    override fun setOnClick() {
        super.setOnClick()
        var timer = Timer()
        binding.etSearch.addTextChangedListener {
            timer.cancel()
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    viewModel.searchMessages(it.toString())

                }
            }, 500)
        }

        binding.etSearch.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && binding.etSearch.text.isNullOrEmpty()) {
                binding.vpMessages.setCurrentItem(0, true)
            } else {
                binding.vpMessages.setCurrentItem(1, true)
            }
        }
    }
}