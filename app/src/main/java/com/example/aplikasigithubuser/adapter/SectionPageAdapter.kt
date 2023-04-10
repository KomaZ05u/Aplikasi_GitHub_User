package com.example.aplikasigithubuser

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.aplikasigithubuser.ui.Follower
import com.example.aplikasigithubuser.ui.Following

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = Follower()
            1 -> fragment = Following()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}