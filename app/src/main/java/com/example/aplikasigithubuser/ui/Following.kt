package com.example.aplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.viewmodel.MainViewModel
import com.example.aplikasigithubuser.adapter.FollowAdapter
import com.example.aplikasigithubuser.databinding.FragmentFollowBinding
import com.example.aplikasigithubuser.data.model.ResponseFollow

class Following : Fragment() {
    private val viewModel by viewModels<MainViewModel>()
    private val adapter = FollowAdapter()

    private lateinit var fragmentFollowBinding: FragmentFollowBinding
    private val _binding get() = fragmentFollowBinding
    private var username: String? = null
    
    companion object {
        private const val KEY_BUNDLE = "USERNAME"
        
        fun getInstance(username: String): Following {
            return Following().apply {
                arguments = Bundle().apply {
                    putString(KEY_BUNDLE, username)
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(KEY_BUNDLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFollowBinding = FragmentFollowBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showViewModel()
        showRecyclerView()
        viewModel.isLoading.observe(viewLifecycleOwner, this::showLoading)
    }

    private fun showRecyclerView() {
        fragmentFollowBinding.followerlayout.layoutManager = LinearLayoutManager(activity)
        fragmentFollowBinding.followerlayout.setHasFixedSize(true)
        fragmentFollowBinding.followerlayout.adapter = adapter

        adapter.setOnItemClickCallback { data -> selectedUser(data) }
    }

    private fun showViewModel() {
        username?.let { viewModel.following(it) }
        viewModel.userFollowing.observe(viewLifecycleOwner) { following ->
            if (following.size != 0) {
                adapter.setData(following)
            } else {
                Toast.makeText(context, "Following Tidak Ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectedUser(user: ResponseFollow) {
        Toast.makeText(context, "User ${user.login} dipilih", Toast.LENGTH_SHORT).show()

        val intent = Intent(activity, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.EXTRA_USER, user.login)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        username?.let { viewModel.following(it) }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            fragmentFollowBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentFollowBinding.progressBar.visibility = View.GONE
        }
    }
}