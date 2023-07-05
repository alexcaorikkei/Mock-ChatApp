package com.example.baseproject.ui.home.friends
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentListFriendsBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.friends.adapter.FriendsRecycleViewAdapter
import com.example.baseproject.ui.home.friends.model.FriendState
import com.example.baseproject.ui.home.friends.model.SortType
import com.example.baseproject.ui.home.friends.model.getFromListFriendModelSortBy
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
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
                    binding.includeProgress.visibility = android.view.View.VISIBLE
                    binding.emptyResult.visibility = android.view.View.INVISIBLE
                    binding.rvFriends.visibility = android.view.View.INVISIBLE
                }
                is Response.Failure -> {
                    binding.includeProgress.visibility = android.view.View.INVISIBLE
                    binding.emptyResult.visibility = android.view.View.VISIBLE
                    binding.rvFriends.visibility = android.view.View.INVISIBLE
                }
                is Response.Success -> {
                    binding.includeProgress.visibility = android.view.View.INVISIBLE
                    if(response.data.isEmpty()) {
                        binding.emptyResult.visibility = android.view.View.VISIBLE
                        binding.rvFriends.visibility = android.view.View.INVISIBLE
                    }
                    else {
                        binding.rvFriends.visibility = android.view.View.VISIBLE
                        val listFriendItemModel = when(states.size) {
                            2 -> getFromListFriendModelSortBy(
                                SortType.SORT_BY_STATE,
                                response.data.filter { friendModel ->
                                    friendModel.state in states
                                }
                            )

                            else -> getFromListFriendModelSortBy(
                                SortType.SORT_BY_NAME,
                                response.data.filter { friendModel ->
                                    friendModel.state in states
                                }
                            )
                        }
                        binding.rvFriends.adapter = FriendsRecycleViewAdapter(listFriendItemModel)
                    }
                }
            }
        }
    }
}