package com.adoptvillage.bridge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.adoptvillage.bridge.fragments.ChatFragment
import com.adoptvillage.bridge.fragments.HomeFragment
import com.adoptvillage.bridge.fragments.ProfileFragment
import kotlinx.android.synthetic.main.home_screen.*

class home_screen : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

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

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper,fragment)
            commit()
        }


}

