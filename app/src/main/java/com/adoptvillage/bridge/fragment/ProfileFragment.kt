package com.adoptvillage.bridge.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adoptvillage.bridge.activity.MainActivity
import com.adoptvillage.bridge.R
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs=context!!.getSharedPreferences(getString(R.string.parent_package_name), Context.MODE_PRIVATE)

        btnLogoutSetOnClickListener()
    }

    private fun btnLogoutSetOnClickListener() {
        btnPSLogout.setOnClickListener {
            prefs.edit().putBoolean(getString(R.string.is_Logged_In),false).apply()
            startActivity(Intent(context, MainActivity::class.java))
        }
    }

}
