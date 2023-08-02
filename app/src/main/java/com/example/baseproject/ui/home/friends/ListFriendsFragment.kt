package com.example.baseproject.ui.home.friends

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentListFriendsBinding
import com.example.baseproject.domain.model.FriendModel
import com.example.baseproject.domain.model.Response
import com.example.baseproject.extension.KEY_ID_RECEIVER
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.friends.adapter.FriendsRecycleViewAdapter
import com.example.baseproject.ui.home.friends.adapter.OnItemClickListener
import com.example.baseproject.domain.model.FriendState
import com.example.baseproject.ui.home.friends.model.SortType
import com.example.baseproject.ui.home.friends.model.getFromListFriendModelSortBy
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFriendsFragment(private var states: List<FriendState>) :
    BaseFragment<FragmentListFriendsBinding, FriendsViewModel>(R.layout.fragment_list_friends),
    OnItemClickListener {

    constructor() : this(listOf())

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList("states", ArrayList(states.map { it.name }))
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getStringArrayList("states")?.forEach() {
            states = states + FriendState.valueOf(it)
        }
    }

    companion object {
        fun newInstance(states: List<FriendState>) = ListFriendsFragment(states)
    }

    private val listFriend = mutableListOf<FriendModel>()

    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: FriendsViewModel by viewModels({ requireParentFragment() })
    override fun getVM() = viewModel
    private var enabledButton = true
    private lateinit var friendsAdapter:FriendsRecycleViewAdapter

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.rvFriends.layoutManager = LinearLayoutManager(requireContext())
        friendsAdapter = FriendsRecycleViewAdapter(
            this,
            listFriend,
            if(states.size == 2) SortType.SORT_BY_STATE else SortType.SORT_BY_NAME
        )
        binding.rvFriends.adapter = friendsAdapter
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.listFriendLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {
                    binding.emptyResult.visibility = View.INVISIBLE
                    binding.rvFriends.visibility = View.INVISIBLE
                }

                is Response.Failure -> {
                    binding.emptyResult.visibility = View.VISIBLE
                    binding.rvFriends.visibility = View.INVISIBLE
                }

                is Response.Success -> {
                    listFriend.clear()
                    listFriend.addAll(
                        response.data.filter { friendModel ->
                            friendModel.state in states
                        }
                    )
                    if (listFriend.isEmpty()) {
                        binding.emptyResult.visibility = View.VISIBLE
                        binding.rvFriends.visibility = View.INVISIBLE
                    } else {
                        binding.emptyResult.visibility = View.INVISIBLE
                        binding.rvFriends.visibility = View.VISIBLE
                        friendsAdapter.notifyChangeList()
                    }
                }
            }
        }
        viewModel.friendStateResponse.observe(viewLifecycleOwner) {
            enabledButton = when (it) {
                is Response.Loading -> false
                is Response.Failure -> true
                is Response.Success -> true
            }
        }
        viewModel.queryResponse.observe(viewLifecycleOwner) {query ->
            val hasData = friendsAdapter.filter(query)
            if(!hasData) binding.emptyResult.visibility = View.VISIBLE
            else binding.emptyResult.visibility = View.INVISIBLE
        }
    }

    override fun onItemClicked(friendData: FriendModel) {
        if(enabledButton) {
            if (friendData.state == FriendState.FRIEND) {
                val bundle = Bundle()
                bundle.putString(KEY_ID_RECEIVER, friendData.uid)
                appNavigation.openHomeToChatScreen(bundle)
            } else {
                getString(R.string.not_friend).toast(requireContext())
            }
        }
    }

    override fun onAcceptClicked(friendData: FriendModel) {
        if (enabledButton) viewModel.acceptFriend(friendData.uid)
    }

    override fun onCancelClicked(friendData: FriendModel) {
        if (enabledButton) viewModel.cancelFriend(friendData.uid)
    }

    override fun onAddNewFriendClicked(friendData: FriendModel) {
        if (enabledButton) viewModel.addFriend(friendData.uid)
    }
}