package com.example.aplikasigithubuser.api

import com.example.aplikasigithubuser.data.model.ResponseDetailUser
import com.example.aplikasigithubuser.data.model.ResponseFollow
import com.example.aplikasigithubuser.data.model.ResponseSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
interface APIService {
    @GET("search/users")
    fun searchUser(
        @Query("q") username:String
    ): Call<ResponseSearch>

    @GET("users/{username}")
    fun detailUser(
        @Path("username") username:String
    ): Call<ResponseDetailUser>

    @GET("users/{username}/followers")
    fun follower(
        @Path("username") username:String
    ): Call<ArrayList<ResponseFollow>>

    @GET("users/{username}/following")
    fun following(
        @Path("username") username:String
    ): Call<ArrayList<ResponseFollow>>
}