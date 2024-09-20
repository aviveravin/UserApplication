package com.example.usersapplication.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.usersapplication.R
import com.example.usersapplication.databinding.LayoutUserItemBinding
import com.example.usersapplication.model.User
import com.example.usersapplication.model.UserData
import com.example.usersapplication.model.UserResponse
import com.squareup.picasso.Picasso

class UserListAdapter(
    private val userClick: UserClick
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    private var userDetails: MutableList<UserData> = mutableListOf()
    private var fullUserList: MutableList<UserData> = mutableListOf()


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(userList: MutableList<UserData>) {
        userDetails = userList
        fullUserList = userList.toMutableList()
        Log.d("TAG", "updateData: check the updateDate $userDetails")
        notifyDataSetChanged()
    }

    fun getCurrentList(): List<UserData> {
        return userDetails
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(filteredList: MutableList<UserData>) {
        userDetails = filteredList
        notifyDataSetChanged()
    }

    // Restore the full list
    @SuppressLint("NotifyDataSetChanged")
    fun restoreFullList() {
        userDetails = fullUserList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutUserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userDetails[position]
        holder.onBind(user)
        with(holder.binding){
            root.setOnClickListener {
                userClick.onUserClick(position, user)
            }
        }
    }

    override fun getItemCount(): Int = userDetails.size

    class ViewHolder(val binding: LayoutUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(user: UserData) {
            // Bind data to your views here
            with(binding){
                tvUserName.text = user.first_name + user.last_name
                tvUserEmail.text = user.email
                Picasso.get()
                    .load(user.avatar) // Load the avatar URL
                    .into(binding.ivProfilePic) // Set into ImageView
            }
        }
    }

    interface UserClick {
        fun onUserClick(position: Int, user: UserData)
    }
}