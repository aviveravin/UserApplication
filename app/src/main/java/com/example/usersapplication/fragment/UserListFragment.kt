package com.example.usersapplication.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.asLiveData
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usersapplication.adapter.UserListAdapter
import com.example.usersapplication.databinding.FragmentUserListBinding
import com.example.usersapplication.model.User
import com.example.usersapplication.model.UserData
import com.example.usersapplication.util.ViewState
import com.example.usersapplication.viewmodel.UserViewModel
import com.example.usersapplication.viewmodel.UserViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserListFragment: Fragment(), UserListAdapter.UserClick {

    private lateinit var binding: FragmentUserListBinding

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var userListAdapter: UserListAdapter

    private val userViewModel by lazy {
        ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        userListAdapter = UserListAdapter(this)
        binding.rvUserList.apply {
            layoutManager = linearLayoutManager
            adapter = userListAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }

        userViewModel.fetchUsers(1)
        setObservers()
        setupSearchView()
        binding.searchView.clearFocus()
    }

    private fun setObservers() {
        userViewModel.getAllUsersFlow.asLiveData()
            .observe(requireActivity()) { viewState ->
                when (viewState) {
                    is ViewState.Loading -> {
                        Log.d("is ViewState.ErrorsData ->", "setObservers: Loading...")

                    }

                    is ViewState.Success -> {
                        Log.d("aviveravin", "bus list")
                        val userList =
                            viewState.data.data // Assuming data is a list of BusService
                        userListAdapter.updateData(userList)
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

    private fun setupSearchView() {
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString().trim()
                filterUsers(searchText)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    private fun filterUsers(query: String) {
        if (query.isEmpty()) {
            userListAdapter.restoreFullList() // Restore the full list when query is empty
        } else {
            val currentList = userListAdapter.getCurrentList()
            val filteredList = currentList.filter {
                it.first_name.contains(query, true) || it.last_name.contains(query, true) || it.email.contains(query, true)
            }
            userListAdapter.filterData(filteredList.toMutableList()) // Update with filtered list
        }
    }


    override fun onUserClick(position: Int, user: UserData) {
        Log.d("aviveravin", "userid: ${user.id}")
        val action = UserListFragmentDirections.actionUserListFragmentToUserDetailFragment(user.id)
        findNavController().navigate(action)
    }
}