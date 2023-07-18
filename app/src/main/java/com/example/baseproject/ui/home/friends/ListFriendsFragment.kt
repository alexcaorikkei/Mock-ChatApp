package com.example.baseproject.ui.home.friends

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentListFriendsBinding
import com.example.baseproject.databinding.ItemFriendBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.extension.KEY_ID_RECEIVER
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.friends.adapter.FriendsRecycleViewAdapter
import com.example.baseproject.ui.home.friends.adapter.OnItemClickListener
import com.example.baseproject.ui.home.friends.model.FriendItemModel
import com.example.baseproject.domain.model.FriendState
import com.example.baseproject.ui.home.friends.model.SortType
import com.example.baseproject.ui.home.friends.model.getFromListFriendModelSortBy
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ListFriendsFragment(private var states: List<FriendState>) :
    BaseFragment<FragmentListFriendsBinding, FriendsViewModel>(R.layout.fragment_list_friends),
    OnItemClickListener {
    constructor() : this(listOf())

    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: FriendsViewModel by activityViewModels()
    private var listFriendItemModel = listOf<FriendItemModel>()
    override fun getVM() = viewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.searchAllUserWithCurrentAccount(viewModel.currentQuery)
    }

    override fun bindingStateView() {
        super.bindingStateView()
        binding.rvFriends.layoutManager = LinearLayoutManager(requireContext())
        viewModel.searchResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                    binding.emptyResult.visibility = View.INVISIBLE
                    binding.rvFriends.visibility = View.INVISIBLE
                }
                is Response.Failure -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.emptyResult.visibility = View.VISIBLE
                    binding.rvFriends.visibility = View.INVISIBLE
                }
                is Response.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    if (response.data.isEmpty()) {
                        binding.emptyResult.visibility = View.VISIBLE
                        binding.rvFriends.visibility = View.INVISIBLE
                    } else {
                        binding.rvFriends.visibility = View.VISIBLE
                        val listFriends = response.data.filter { friendModel ->
                            friendModel.state in states
                        }.toMutableList()
                        listFriendItemModel = when (states.size) {
                            2 -> getFromListFriendModelSortBy(
                                SortType.SORT_BY_STATE,
                                listFriends
                            )
                            else -> getFromListFriendModelSortBy(
                                SortType.SORT_BY_NAME,
                                listFriends
                            )
                        }
                        binding.rvFriends.adapter = FriendsRecycleViewAdapter(
                            listFriendItemModel,
//                            this
                        )
                    }
                }
            }
        }
        viewModel.friendChangeStateResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                is Response.Failure -> {
                    binding.progressCircular.visibility = View.INVISIBLE
                    "Error: ${it.e}".toast(requireContext())
                }
                is Response.Success -> {
                    binding.progressCircular.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.searchAllUserWithCurrentAccount(viewModel.currentQuery)
        }
    }

    override fun onItemClicked(position: Int, view: ItemFriendBinding) {
        val bundle = Bundle()
        bundle.putString(KEY_ID_RECEIVER, listFriendItemModel[position].friendModel!!.uid)
        appNavigation.openHomeToChatScreen(bundle)
    }

    override fun onAcceptClicked(position: Int, view: ItemFriendBinding) {
        viewModel.acceptFriend(listFriendItemModel[position].friendModel!!.uid)
    }

    override fun onCancelClicked(position: Int, view: ItemFriendBinding) {
        viewModel.cancelFriend(listFriendItemModel[position].friendModel!!.uid)
    }

    override fun onAddNewFriendClicked(position: Int, view: ItemFriendBinding) {
        viewModel.addFriend(listFriendItemModel[position].friendModel!!.uid)
    }
}