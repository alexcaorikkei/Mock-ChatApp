package com.example.baseproject.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.R
import com.example.baseproject.domain.model.Response
import com.example.baseproject.domain.repository.FriendRepository
import com.example.baseproject.domain.model.FriendModel
import com.example.baseproject.domain.model.FriendState
import com.example.baseproject.extension.toViWithoutAccent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.values
import kotlinx.coroutines.tasks.await

class FriendRepositoryImpl : FriendRepository {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    override fun getFriends(): LiveData<Response<List<FriendModel>>> {
        val friendsResponse = MutableLiveData<Response<List<FriendModel>>>()
        database.reference.child("users").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<FriendModel>()
                snapshot.children.forEach {user ->
                    if(user.key.toString() != auth.uid) {
                        val friend = FriendModel(
                            uid = user.key.toString(),
                            displayName = user.child("profile").child("display_name").value.toString(),
                            profilePicture = user.child("profile").child("profile_picture").value.toString(),
                            state = FriendState.NONE
                        )
                        friends.add(friend)
                    }
                }
                database.reference.child("users").child(auth.uid!!).child("friends").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {friend ->
                            val friendUid = friend.key.toString()
                            val friendState = FriendState.fromString(friend.child("state").value.toString())
                            friends.find { it.uid == friendUid }?.state = friendState
                        }
                        friendsResponse.postValue(Response.Success(friends))
                    }
                    override fun onCancelled(error: DatabaseError) {
                        friendsResponse.postValue(Response.Failure(error.toException()))
                    }
                })
            }
            override fun onCancelled(error: DatabaseError) {
                friendsResponse.postValue(Response.Failure(error.toException()))
            }
        })
        return friendsResponse
    }

    override suspend fun addFriend(friendUid: String): Response<Boolean> {
        return try {
            val userFriendReference = database.reference.child("users").child(auth.uid!!).child("friends")
            if(userFriendReference.child(friendUid).child("state").get().await().value == null) {
                userFriendReference.child(friendUid).child("state")
                    .setValue(FriendState.ADDED.toString()).await()
            }

            val friendFriendReference = database.reference.child("users").child(friendUid).child("friends")
            if(friendFriendReference.child(auth.uid!!).child("state").get().await().value == null) {
                friendFriendReference.child(auth.uid!!).child("state")
                    .setValue(FriendState.REQUEST.toString()).await()
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun acceptFriend(friendUid: String): Response<Boolean> {
        return try {
            val userFriendReference = database.reference.child("users").child(auth.uid!!).child("friends")
            val friendFriendReference = database.reference.child("users").child(friendUid).child("friends")
            if(userFriendReference.child(friendUid).child("state").get().await().value.toString() == FriendState.REQUEST.toString()) {
                userFriendReference.child(friendUid).child("state")
                    .setValue(FriendState.FRIEND.toString()).await()
                friendFriendReference.child(auth.uid!!).child("state")
                    .setValue(FriendState.FRIEND.toString()).await()
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun removeFriend(friendUid: String): Response<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun cancelFriend(friendUid: String): Response<Boolean> {
        return try {
            val userFriendReference = database.reference.child("users").child(auth.uid!!).child("friends")
            if(userFriendReference.child(friendUid).child("state").get().await().value.toString() == FriendState.ADDED.toString()) {
                userFriendReference.child(friendUid)
                    .removeValue().await()
            }

            val friendFriendReference = database.reference.child("users").child(friendUid).child("friends")
            if(friendFriendReference.child(auth.uid!!).child("state").get().await().value.toString() == FriendState.REQUEST.toString()) {
                friendFriendReference.child(auth.uid!!)
                    .removeValue().await()
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

}