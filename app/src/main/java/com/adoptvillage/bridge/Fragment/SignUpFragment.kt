package com.adoptvillage.bridge.Fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adoptvillage.bridge.Activity.systemGray
import com.adoptvillage.bridge.Activity.systemViolet
import com.adoptvillage.bridge.Models.Register
import com.adoptvillage.bridge.Models.RegisterDefaultResponse
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.Service.RetrofitClient
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var role:Int=0
private var SIGNUPFRAGTAG="SIGNUPFRAGTAG"

class SignUpFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLoginSetOnClickListener()
        btnActionSetOnClickListener()
        btnDonorSetOnClickListener()
        btnRecipientSetOnClickListener()
        btnSignUpSetOnClickListener()
        btnUniversitySetOnClickListener()
    }

    private fun btnUniversitySetOnClickListener() {
        btnSUniversity.setOnClickListener {
            btnSUniversity.setBackgroundColor(Color.parseColor(systemViolet))
            btnSUniversity.setTextColor(Color.WHITE)
            btnSRecipient.setBackgroundColor(Color.parseColor(systemGray))
            btnSRecipient.setTextColor(Color.parseColor(systemViolet))
            btnSDonor.setBackgroundColor(Color.parseColor(systemGray))
            btnSDonor.setTextColor(Color.parseColor(systemViolet))
            role=2
        }
    }

    private fun btnSignUpSetOnClickListener() {  }

    private fun btnRecipientSetOnClickListener() {
        btnSRecipient.setOnClickListener {
            btnSRecipient.setBackgroundColor(Color.parseColor(systemViolet))
            btnSRecipient.setTextColor(Color.WHITE)
            btnSDonor.setBackgroundColor(Color.parseColor(systemGray))
            btnSDonor.setTextColor(Color.parseColor(systemViolet))
            btnSUniversity.setBackgroundColor(Color.parseColor(systemGray))
            btnSUniversity.setTextColor(Color.parseColor(systemViolet))
            role=1
        }
    }

    private fun btnDonorSetOnClickListener() {
        btnSDonor.setOnClickListener {
            btnSDonor.setBackgroundColor(Color.parseColor(systemViolet))
            btnSDonor.setTextColor(Color.WHITE)
            btnSRecipient.setBackgroundColor(Color.parseColor(systemGray))
            btnSRecipient.setTextColor(Color.parseColor(systemViolet))
            btnSUniversity.setBackgroundColor(Color.parseColor(systemGray))
            btnSUniversity.setTextColor(Color.parseColor(systemViolet))
            role=0
        }
    }

    private fun btnActionSetOnClickListener() {
        btnSAction.setOnClickListener {

            val name = etSName.text.toString().trim()
            val email = etSEmail.text.toString().trim()
            val password = etSPassword.text.toString().trim()
            val obj = Register(name, email, password)

            RetrofitClient.instance.registerUser(obj).enqueue(object :
                Callback<RegisterDefaultResponse> {
                override fun onResponse(
                    call: Call<RegisterDefaultResponse>,
                    response: Response<RegisterDefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.i(SIGNUPFRAGTAG, response.toString())
                        Log.i(SIGNUPFRAGTAG, response.body()?.message)
                        Toast.makeText(context,response.body()?.message,Toast.LENGTH_SHORT).show()
                    } else {
                        Log.i(SIGNUPFRAGTAG, response.toString())
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(SIGNUPFRAGTAG, jObjError.getString("message"))
                        Toast.makeText(context,jObjError.getString("message"),Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<RegisterDefaultResponse>, t: Throwable) {
                    Log.i(SIGNUPFRAGTAG,t.message)
                }
            })
        }
    }

    private fun btnLoginSetOnClickListener() {
        btnSLogin.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_main, LogInFragment())?.commit()
        }
    }
}

