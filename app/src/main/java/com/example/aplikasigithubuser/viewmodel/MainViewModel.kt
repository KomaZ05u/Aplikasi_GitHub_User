package com.example.aplikasigithubuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasigithubuser.api.APIConfig
import com.example.aplikasigithubuser.data.model.Items
import com.example.aplikasigithubuser.data.model.ResponseDetailUser
import com.example.aplikasigithubuser.data.model.ResponseFollow
import com.example.aplikasigithubuser.data.model.ResponseSearch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _searchList = MutableLiveData<ArrayList<Items>>()
    val searchList: LiveData<ArrayList<Items>> = _searchList

    private val _detailUser = MutableLiveData<ResponseDetailUser>()
    val detailUser: LiveData<ResponseDetailUser> = _detailUser

    private val _follower = MutableLiveData<ArrayList<ResponseFollow>>()
    val userFollower: LiveData<ArrayList<ResponseFollow>> = _follower

    private val _following = MutableLiveData<ArrayList<ResponseFollow>>()
    val userFollowing: LiveData<ArrayList<ResponseFollow>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchUser(username: String) {
        _isLoading.value = true
        val client = APIConfig.getApiService().searchUser(username)
        client.enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _searchList.value = ArrayList(responseBody.items)
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun detailUser(username: String) {

        _isLoading.value = true
        val client = APIConfig.getApiService().detailUser(username)
        client.enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(
                call: Call<ResponseDetailUser>,
                response: Response<ResponseDetailUser>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })

    }

    fun follower(username: String) {
        _isLoading.value = true
        val client = APIConfig.getApiService().follower(username)
        client.enqueue(object : Callback<ArrayList<ResponseFollow>> {
            override fun onResponse(
                call: Call<ArrayList<ResponseFollow>>,
                response: Response<ArrayList<ResponseFollow>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _follower.value = response.body()
                }
            }

            override fun onFailure(
                call: Call<ArrayList<ResponseFollow>>,
                t: Throwable
            ) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun following(username: String) {
        _isLoading.value = true
        val client = APIConfig.getApiService().following(username)
        client.enqueue(object : Callback<ArrayList<ResponseFollow>> {
            override fun onResponse(
                call: Call<ArrayList<ResponseFollow>>,
                response: Response<ArrayList<ResponseFollow>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _following.value = response.body()
                }
            }

            override fun onFailure(
                call: Call<ArrayList<ResponseFollow>>,
                t: Throwable
            ) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}