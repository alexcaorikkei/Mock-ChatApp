package com.example.baseproject.ui.home.friends

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentListFriendsBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.friends.adapter.FriendsRecycleViewAdapter
import com.example.baseproject.ui.home.friends.model.FriendModel
import com.example.baseproject.ui.home.friends.model.FriendState
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class ListFriendsFragment(private val states: List<FriendState>) : BaseFragment<FragmentListFriendsBinding, FriendsViewModel>(R.layout.fragment_list_friends) {
    constructor() : this(listOf())
    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: FriendsViewModel by activityViewModels()
    override fun getVM() = viewModel

    override fun bindingAction() {
        super.bindingAction()
    }

    override fun bindingStateView() {
        super.bindingStateView()
        binding.rvFriends.layoutManager = LinearLayoutManager(requireContext())
        viewModel.searchResponse.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Loading -> {
                }
                is Response.Failure -> {
                }
                is Response.Success -> {
                    binding.rvFriends.adapter = FriendsRecycleViewAdapter(
                        response.data as MutableList<FriendModel>,
                        states
                    )
                }
            }
        }
    }
}