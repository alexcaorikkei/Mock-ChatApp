package com.example.baseproject.ui.home.friends

import androidx.fragment.app.activityViewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentListFriendsBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.friends.adapter.FriendsRecycleViewAdapter
import com.example.baseproject.ui.home.friends.model.FriendModel
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class ListFriendsFragment(private val position: Int) : BaseFragment<FragmentListFriendsBinding, FriendsViewModel>(R.layout.fragment_list_friends) {
    constructor() : this(0)
    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: FriendsViewModel by activityViewModels()
    override fun getVM() = viewModel

    override fun bindingAction() {
        super.bindingAction()
    }

    override fun bindingStateView() {
        super.bindingStateView()
        binding.rvFriends.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        val searchResponse = when(position) {
            0 -> viewModel.searchFriendsResponse
            1 -> viewModel.searchAllUserResponse
            2 -> viewModel.searchRequestResponse
            else -> viewModel.searchFriendsResponse
        }
        searchResponse.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Loading -> {
                }
                is Response.Failure -> {
                }
                is Response.Success -> {
                    Timber.d("response.data: ${response.data}")
                    binding.rvFriends.adapter = FriendsRecycleViewAdapter(response.data as MutableList<FriendModel>)
                }
            }
        }
    }
}