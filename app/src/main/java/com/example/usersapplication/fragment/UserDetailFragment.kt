package com.example.usersapplication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import com.example.usersapplication.databinding.FragmentUserDetailBinding
import com.example.usersapplication.util.ViewState
import com.example.usersapplication.viewmodel.UserViewModel
import com.example.usersapplication.viewmodel.UserViewModelFactory
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserDetailFragment: Fragment() {

    private lateinit var binding: FragmentUserDetailBinding

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var userViewModel: UserViewModel

    private val args: UserDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        // Use the userId to fetch user details
        val userId = UserDetailFragmentArgs.fromBundle(requireArguments()).userId
        Log.d("aviveravin", "userid in detailfragment: $userId")
        userViewModel.fetchUserById(userId)

        userViewModel.getUserFlow.asLiveData().observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    Log.d("is ViewState.ErrorsData ->", "setObservers: Loading...")

                }

                is ViewState.Success -> {
                    val userData = viewState.data.data
                    Log.d("UserDetailFragment", "User data received: ${viewState.data}")
                    binding.tvUserName.text = "${userData.first_name} ${userData.last_name}"
                    binding.tvUserEmail.text = userData.email
                    Picasso.get()
                        .load(userData.avatar)
                        .into(binding.ivProfilePic)
                }

                is ViewState.NetworkFailed -> {
                    Log.d("aviveravin", "setObservers: Network failed")
                    Toast.makeText(
                        requireContext(),
                        "Constants.NO_INTERNET_MESSAGE",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is ViewState.ErrorsData -> {
                    Log.d("aviveravin", "setObservers: ${viewState.errorData}")
                    // Handle the error state here, you can access the error message using viewState.errorMessage
                }

                else -> {
                    Log.d("aviveravin", "else: $viewState")
                }
            }
        }
    }

}