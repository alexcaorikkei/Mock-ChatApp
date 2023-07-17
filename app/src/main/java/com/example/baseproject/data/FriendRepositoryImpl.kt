package com.example.baseproject.data

import com.example.baseproject.R
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.FriendRepository
import com.example.baseproject.domain.model.FriendModel
import com.example.baseproject.domain.model.FriendState
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

    override suspend fun sendFriendRequest(friendId: String): Response<String> {
        return try {
            database.reference.child("users").apply {
                val userProfile = child(auth.uid!!).child("profile").get().await()
                val friendProfile = child(friendId).child("profile").get().await()
                child(auth.uid!!).child("added").child(friendId).apply {
                    child("display_name").setValue(friendProfile.child("display_name").value)
                    child("profile_picture").setValue(friendProfile.child("profile_picture").value)
                }
                child(friendId).child("request").child(auth.uid!!).apply {
                    child("display_name").setValue(userProfile.child("display_name").value)
                    child("profile_picture").setValue(userProfile.child("profile_picture").value)
                }
            }
            Response.Success(R.string.friend_request_sent.toString())
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun acceptFriendRequest(friendId: String): Response<String> {
        return try {
            database.reference.child("users").apply {
                val userProfile = child(friendId).child("added").child(auth.uid!!).get().await()
                val friendProfile = child(auth.uid!!).child("request").child(friendId).get().await()
                child(auth.uid!!).child("request").child(friendId).setValue(null)
                child(auth.uid!!).child("friends").child(friendId).apply {
                    child("display_name").setValue(friendProfile.child("display_name").value)
                    child("profile_picture").setValue(friendProfile.child("profile_picture").value)
                }
                child(friendId).child("added").child(auth.uid!!).setValue(null)
                child(friendId).child("friends").child(auth.uid!!).apply {
                    child("display_name").setValue(userProfile.child("display_name").value)
                    child("profile_picture").setValue(userProfile.child("profile_picture").value)
                }
            }
            Response.Success(R.string.friend_request_accepted.toString())
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun cancelFriendRequest(friendId: String): Response<String> {
        return try {
            database.reference.child("users").apply {
                child(auth.uid!!).child("added").child(friendId).setValue(null)
                child(friendId).child("request").child(auth.uid!!).setValue(null)
            }
            Response.Success(R.string.friend_request_canceled.toString())
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    private fun String.notIn(listFriends: List<FriendModel>): Boolean {
        listFriends.forEach {
            if (it.uid == this) {
                return false
            }
        }
        return true
    }
    private suspend fun searchByState(state: FriendState, query: String, listFriends: MutableList<FriendModel>) {
        val friends = database.reference.child("users").child(auth.uid!!).child(state.toString()).get().await()
        friends.children.forEach { friend ->
            if(friend.child("display_name").value.toString().contains(query)) {
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
            if(userSnapshot.key.toString().notIn(listFriends)
                && userSnapshot.child("profile").child("display_name").value.toString().contains(query)
                && userSnapshot.key.toString() != auth.uid!!) {
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