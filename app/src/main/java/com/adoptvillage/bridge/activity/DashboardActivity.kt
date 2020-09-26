package com.adoptvillage.bridge.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.adoptvillage.bridge.fragment.ChatFragment
import com.adoptvillage.bridge.fragment.HomeFragment
import com.adoptvillage.bridge.fragment.ProfileFragment
import com.adoptvillage.bridge.R
import kotlinx.android.synthetic.main.activity_dashboard.*

/* DASHBOARD ACTIVITY
contains 3 fragment -
Chat fragment
Home Fragment
Profile Fragment

called from Login Fragment
 */

private lateinit var prefs:SharedPreferences

class DashboardActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragment()
    private val chatFragment = ChatFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        //shared preference
        prefs=this.getSharedPreferences(getString(R.string.parent_package_name), Context.MODE_PRIVATE)

        //function for replacing fragments
        makeCurrentFragment(homeFragment)

        //item listener for bottom navigation
        btmNavigationSetOnItemClickListener()

    }

    private fun btmNavigationSetOnItemClickListener() {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.ic_home-> makeCurrentFragment(homeFragment)
                R.id.ic_profile->makeCurrentFragment(profileFragment)
                R.id.ic_chats->makeCurrentFragment(chatFragment)
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.fl_wrapper,fragment)
        commit()
    }
}


