package com.adoptvillage.bridge.Activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adoptvillage.bridge.Models.Register
import com.adoptvillage.bridge.Models.RegisterDefaultResponse
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.Service.RetrofitClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val systemViolet = "#5856D6"
const val systemGray = "#e2e2e2"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLoginClickListener()
        btnSignUpClickListener()
        btnDonorClickListener()
        btnRecipientClickListener()
        btnUniversityClickListener()
        btnActionClickListener()
    }

    private fun btnActionClickListener() {
        btnAction.setOnClickListener {
            //Toast.makeText(this, "Button Pressed", Toast.LENGTH_SHORT).show()
            val name = "Abhi"
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val obj = Register(name, email, password)

            RetrofitClient.instance.registerUser(obj).enqueue(object :
                Callback<RegisterDefaultResponse> {
                override fun onResponse(
                    call: Call<RegisterDefaultResponse>,
                    response: Response<RegisterDefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<RegisterDefaultResponse>, t: Throwable) {
                }

            })
        }
    }

    private fun btnUniversityClickListener() {
        btnUniversity.setOnClickListener {
            btnUniversity.setBackgroundColor(Color.parseColor(systemViolet))
            btnUniversity.setTextColor(Color.WHITE)
            btnRecipient.setBackgroundColor(Color.parseColor(systemGray))
            btnRecipient.setTextColor(Color.parseColor(systemViolet))
            btnDonor.setBackgroundColor(Color.parseColor(systemGray))
            btnDonor.setTextColor(Color.parseColor(systemViolet))
        }
    }

    private fun btnRecipientClickListener() {
        btnRecipient.setOnClickListener {
            btnRecipient.setBackgroundColor(Color.parseColor(systemViolet))
            btnRecipient.setTextColor(Color.WHITE)
            btnDonor.setBackgroundColor(Color.parseColor(systemGray))
            btnDonor.setTextColor(Color.parseColor(systemViolet))
            btnUniversity.setBackgroundColor(Color.parseColor(systemGray))
            btnUniversity.setTextColor(Color.parseColor(systemViolet))
        }
    }

    private fun btnDonorClickListener() {
        btnDonor.setOnClickListener {
            btnDonor.setBackgroundColor(Color.parseColor(systemViolet))
            btnDonor.setTextColor(Color.WHITE)
            btnRecipient.setBackgroundColor(Color.parseColor(systemGray))
            btnRecipient.setTextColor(Color.parseColor(systemViolet))
            btnUniversity.setBackgroundColor(Color.parseColor(systemGray))
            btnUniversity.setTextColor(Color.parseColor(systemViolet))
        }
    }

    private fun btnSignUpClickListener() {
        btnSignUp.setOnClickListener {
            tilConfirmPassword.visibility = View.VISIBLE
            etConfirmPassword.isEnabled = true
            clLogin.setBackgroundResource(R.drawable.inactive_back)
            clSignUp.setBackgroundResource(R.drawable.pressed_back)
            btnLogin.setTextColor(Color.parseColor(systemViolet))
            btnSignUp.setTextColor(Color.WHITE)
            btnAction.text = getString(R.string.signup)
        }
    }

    private fun btnLoginClickListener() {
        btnLogin.setOnClickListener {
            tilConfirmPassword.visibility = View.INVISIBLE
            etConfirmPassword.isEnabled = false
            clLogin.setBackgroundResource(R.drawable.pressed_back)
            clSignUp.setBackgroundResource(R.drawable.inactive_back)
            btnLogin.setTextColor(Color.WHITE)
            btnSignUp.setTextColor(Color.parseColor(systemViolet))
            btnAction.text = getString(R.string.login)
        }
    }
}