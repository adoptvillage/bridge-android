package com.adoptvillage.bridge.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.fragment.chatScreenFragment.chatFragment
import com.adoptvillage.bridge.fragment.homeFragment.HomeFragment

class ChatScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)

        supportFragmentManager.beginTransaction().replace(R.id.fl_wrapper, chatFragment()).commit()
    }
}