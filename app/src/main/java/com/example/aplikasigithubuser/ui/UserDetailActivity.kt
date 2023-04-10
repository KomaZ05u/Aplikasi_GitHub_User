package com.example.aplikasigithubuser.ui

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.viewmodel.MainViewModel
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.adapter.SectionsPagerAdapter
import com.example.aplikasigithubuser.data.model.ResponseDetailUser
import com.example.aplikasigithubuser.data.model.local.DatabaseContract
import com.example.aplikasigithubuser.data.model.local.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.example.aplikasigithubuser.data.model.local.DatabaseHelper
import com.example.aplikasigithubuser.data.model.local.FavoriteHelper
import com.example.aplikasigithubuser.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var userDetailActivityBinding: ActivityUserDetailBinding
    private val viewModel by viewModels<MainViewModel>()

    private var listFav = ArrayList<ResponseDetailUser>()
    private lateinit var helper: FavoriteHelper

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
        var username = String()
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private fun showViewModel() {
//        viewModel.detailUser(username)
//        viewModel.detailUser.observe(this) { detailUser ->
//            Glide.with(this)
//                .load(detailUser.avatarUrl)
//                .skipMemoryCache(true)
//                .into(userDetailActivityBinding.avatar)
//
//            userDetailActivityBinding.nama.text = detailUser.name
//            userDetailActivityBinding.username.text = detailUser.login
//            userDetailActivityBinding.followerValue.text = detailUser.followers
//            userDetailActivityBinding.followingValue.text = detailUser.following
//        }

        viewModel.detailUser(username)
        viewModel.detailUser.observe(this) { detailUser ->
            Glide.with(this)
                .load(detailUser.avatarUrl)
                .skipMemoryCache(true)
                .into(userDetailActivityBinding.avatar)

            userDetailActivityBinding.apply {
                nama.text = detailUser.name
                username.text = detailUser.login
                followerValue.text = detailUser.followers
                followingValue.text = detailUser.following
            }

            if (favoriteExist(username)) {
                userDetailActivityBinding.toggleFavorite.isFavorite = true
                userDetailActivityBinding.toggleFavorite.setOnFavoriteChangeListener { _, favorite ->
                    if (favorite) {
                        listFav = helper.queryAll()
                        helper.insert(detailUser)
                    } else {
                        listFav = helper.queryAll()
                        helper.delete(username)
                    }
                }
            } else {
                userDetailActivityBinding.toggleFavorite.setOnFavoriteChangeListener { _, favorite ->
                    if (favorite) {
                        listFav = helper.queryAll()
                        helper.insert(detailUser)
                    } else {
                        listFav = helper.queryAll()
                        helper.delete(username)
                    }
                }
            }
        }
    }

    private fun favoriteExist(user: String): Boolean {
        val choose: String = DatabaseContract.FavoriteColumns.LOGIN + " =?"
        val chooseArg = arrayOf(user)
        val limit = "1"

        helper = FavoriteHelper(this)
        helper.open()

        val dataBaseHelper = DatabaseHelper(this@UserDetailActivity)
        val database: SQLiteDatabase = dataBaseHelper.writableDatabase
        val cursor: Cursor =
            database.query(TABLE_NAME, null, choose, chooseArg, null, null, null, limit)
        val exists: Boolean = cursor.count > 0
        cursor.close()

        database.close()
        return exists
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDetailActivityBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(userDetailActivityBinding.root)

        val id = intent.getIntExtra(EXTRA_ID, 0)
        username = intent.getStringExtra(EXTRA_USER).toString()
        val sectionPageAdapter = SectionsPagerAdapter(this, username)
        val viewPager: ViewPager2 = userDetailActivityBinding.viewPager
        viewPager.adapter = sectionPageAdapter
        val tabs: TabLayout = userDetailActivityBinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        
        showViewModel()
        viewModel.isLoading.observe(this, this::showLoading)
    }

    private fun showLoading(isLoading: Boolean) =
        if (isLoading) {
            userDetailActivityBinding.progressBar.visibility = View.VISIBLE
        } else {
            userDetailActivityBinding.progressBar.visibility = View.GONE
        }

}