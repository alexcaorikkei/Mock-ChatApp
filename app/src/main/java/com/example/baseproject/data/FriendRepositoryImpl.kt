package com.example.baseproject.data

import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.FriendRepository
import com.example.baseproject.ui.home.friends.model.FriendModel
import com.example.baseproject.ui.home.friends.model.FriendState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FriendRepositoryImpl : FriendRepository {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    override suspend fun searchAllUserWithCurrentAccount(query: String): Response<List<FriendModel>> {
        return try {
            val listFriends = mutableListOf<FriendModel>()
            searchByState(FriendState.ADDED, query, listFriends)
            searchByState(FriendState.REQUEST, query, listFriends)
            searchByState(FriendState.FRIEND, query, listFriends)
            searchAll(query, listFriends)
            Response.Success(listFriends)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
    private fun String.notIn(listFriends: List<FriendModel>): Boolean {
        listFriends.forEach {
            if (it.displayName == this) {
                return false
            }
        }
        return true
    }
    private suspend fun searchByState(state: FriendState, query: String, listFriends: MutableList<FriendModel>) {
        val friends = database.reference.child("users").child(auth.uid!!).child(state.toString()).get().await()
        friends.children.forEach { friend ->
            if(friend.child("display_name").toString().contains(query)) {
                listFriends.add(
                    FriendModel(
                        friend.key.toString(),
                        friend.child("display_name").value.toString(),
                        friend.child("profile_picture").value.toString(),
                        state,
                    )
                )
            }
        }
    }
    private suspend fun searchAll(query: String, listFriends: MutableList<FriendModel>) {
        val friends = database.reference.child("users").get().await()
        friends.children.forEach { userSnapshot ->
            if(userSnapshot.key.toString().notIn(listFriends) && userSnapshot.child("profile").child("display_name").value.toString().contains(query)) {
                listFriends.add(
                    FriendModel(
                        userSnapshot.key.toString(),
                        userSnapshot.child("profile").child("display_name").value.toString(),
                        userSnapshot.child("profile").child("profile_picture").value.toString(),
                        FriendState.NONE
                    )
                )
            }
        }
    }
}