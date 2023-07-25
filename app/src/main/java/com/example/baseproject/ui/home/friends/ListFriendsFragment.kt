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
import com.example.baseproject.extension.toViWithoutAccent
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
    private var enabledButton = true
    private val friendsAdapter = FriendsRecycleViewAdapter(this)

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

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.rvFriends.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFriends.adapter = friendsAdapter
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.listFriend.observe(viewLifecycleOwner) { response ->
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
                    viewModel.queryData.observe(viewLifecycleOwner) { query ->
                        val listFriends = response.data.filter { friendModel ->
                            (friendModel.state in states
                                    && friendModel.displayName.toViWithoutAccent()
                                .contains(query.toViWithoutAccent()))
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
                        if(listFriendItemModel.isEmpty()) {
                            binding.emptyResult.visibility = View.VISIBLE
                            binding.rvFriends.visibility = View.INVISIBLE
                        } else {
                            binding.emptyResult.visibility = View.INVISIBLE
                            binding.rvFriends.visibility = View.VISIBLE
                            friendsAdapter.submitList(listFriendItemModel)
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
        }
    }

    override fun onItemClicked(position: Int, view: ItemFriendBinding) {
        if(listFriendItemModel[position].friendModel != null) {
            if(listFriendItemModel[position].friendModel!!.state == FriendState.FRIEND) {
                val bundle = Bundle()
                bundle.putString(KEY_ID_RECEIVER, listFriendItemModel[position].friendModel!!.uid)
                appNavigation.openHomeToChatScreen(bundle)
            } else {
                getString(R.string.not_friend).toast(requireContext())
            }
        }
    }

    override fun onAcceptClicked(position: Int, view: ItemFriendBinding) {
        if(enabledButton) viewModel.acceptFriend(listFriendItemModel[position].friendModel!!.uid)
    }

    override fun onCancelClicked(position: Int, view: ItemFriendBinding) {
        if(enabledButton) viewModel.cancelFriend(listFriendItemModel[position].friendModel!!.uid)
    }

    override fun onAddNewFriendClicked(position: Int, view: ItemFriendBinding) {
        if(enabledButton) viewModel.addFriend(listFriendItemModel[position].friendModel!!.uid)
    }
}