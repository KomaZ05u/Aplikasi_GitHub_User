package com.example.aplikasigithubuser.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submission2_rayhaan_fundamentalandroid_v2.adapter.FavoriteAdapter
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.data.model.ResponseDetailUser
import com.example.aplikasigithubuser.data.model.local.FavoriteHelper
import com.example.aplikasigithubuser.databinding.ActivityFavoriteUserBinding

class FavoriteUser : AppCompatActivity() {
    private val adapter = FavoriteAdapter()
    private lateinit var helper: FavoriteHelper
    private var listFavorite: ArrayList<ResponseDetailUser> = ArrayList()

    private lateinit var activityFavoriteUserBinding: ActivityFavoriteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFavoriteUserBinding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(activityFavoriteUserBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        helper = FavoriteHelper.getInstance(applicationContext)
        helper.open()

        showRecyclerView()
        getDataFavorite()
    }

    private fun showRecyclerView() {
        activityFavoriteUserBinding.userList.layoutManager =
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this, 2)
            } else {
                LinearLayoutManager(this)
            }

        activityFavoriteUserBinding.userList.setHasFixedSize(true)
        activityFavoriteUserBinding.userList.adapter = adapter

        adapter.setOnItemClickCallback { data -> selectedUser(data) }
    }

    private fun selectedUser(user: ResponseDetailUser) {
        val intent = Intent(this, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.EXTRA_USER, user.login)
        startActivity(intent)
    }

    private fun getDataFavorite() {
        listFavorite = helper.queryAll()
        if (listFavorite.size > 0) {
            activityFavoriteUserBinding.userList.visibility = View.VISIBLE
            activityFavoriteUserBinding.tvNotFound.visibility = View.GONE
            adapter.setData(listFavorite)
        } else {
            activityFavoriteUserBinding.userList.visibility = View.GONE
            activityFavoriteUserBinding.tvNotFound.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        @Suppress("DEPRECATION")
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        getDataFavorite()
    }
}