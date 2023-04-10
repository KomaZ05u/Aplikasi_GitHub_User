package com.example.aplikasigithubuser.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    var avatar:Int,
    var username:String,
    var name:String,
    var follower:Int,
    var following:Int
): Parcelable