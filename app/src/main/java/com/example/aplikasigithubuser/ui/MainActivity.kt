package com.example.aplikasigithubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.viewmodel.MainViewModel
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.adapter.UserAdapter
import com.example.aplikasigithubuser.databinding.ActivityMainBinding
import com.example.aplikasigithubuser.data.model.Items
import com.example.aplikasigithubuser.setting.ViewModelFact
import com.example.aplikasigithubuser.setting.settingpref
import com.example.aplikasigithubuser.viewmodel.SettingViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    val mainViewModel by viewModels<MainViewModel>()
    val adapter = UserAdapter()

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    private lateinit var viewModelSetting: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        darkModeCheck()
        showViewModel()
        showRecyclerView()
        mainViewModel.isLoading.observe(this, this::showLoading)

        activityMainBinding.favButton.setOnClickListener {
            val intent = Intent(this, FavoriteUser::class.java)
            startActivity(intent)
        }
    }

    private fun showViewModel() {
        mainViewModel.searchList.observe(this) { searchList ->
            if (searchList.size != 0) {
                adapter.setData(searchList)
            } else {
                Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val close = menu.findItem(R.id.search)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = ("Username")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        close.icon?.setVisible(false, false)
        return true
    }

    private fun showRecyclerView() {
        val appOrientation = applicationContext.resources.configuration.orientation
        activityMainBinding.userList.layoutManager =
            when (appOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                true -> GridLayoutManager(this, 2)
                false -> LinearLayoutManager(this)
            }

        activityMainBinding.userList.setHasFixedSize(true)
        activityMainBinding.userList.adapter = adapter

        adapter.setOnItemClickCallback { data -> selectedUser(data) }
    }

    private fun selectedUser(user: Items) {
        Toast.makeText(this, "User ${user.login} dipilih", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.EXTRA_USER, user.login)
        intent.putExtra(UserDetailActivity.EXTRA_ID, user.id)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            activityMainBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityMainBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun darkModeCheck() {
        val pref = settingpref.getInstance(dataStore)
        viewModelSetting =
            ViewModelProvider(this, ViewModelFact(pref))[SettingViewModel::class.java]

        viewModelSetting.getThemeSettings().observe(this@MainActivity) { isDarkModeActive ->
            if (isDarkModeActive) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}