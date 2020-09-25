package com.adoptvillage.bridge.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.fragment.app.Fragment
import com.adoptvillage.bridge.Fragment.ChatFragment
import com.adoptvillage.bridge.Fragment.HomeFragment
import com.adoptvillage.bridge.Fragment.ProfileFragment
import com.adoptvillage.bridge.R
import kotlinx.android.synthetic.main.activity_dashboard.*

private lateinit var prefs:SharedPreferences

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        prefs=this.getSharedPreferences(getString(R.string.parent_package_name), Context.MODE_PRIVATE)

        val Home_Fragment = HomeFragment()
        val Profile_Fragment = ProfileFragment()
        val Chat_Fragment = ChatFragment()

        makeCurrentFragment(Home_Fragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.ic_home-> makeCurrentFragment(Home_Fragment)
                R.id.ic_profile->makeCurrentFragment(Profile_Fragment)
                R.id.ic_chats->makeCurrentFragment(Chat_Fragment)
            }
            true

    }



}

    private fun makeCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.fl_wrapper,fragment)
        commit()
    }

}


        btnLogoutSetOnClickListener()

    }

    private fun btnLogoutSetOnClickListener() {
        btnLogout.setOnClickListener {
            prefs.edit().putBoolean(getString(R.string.is_Logged_In),false).apply()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}

