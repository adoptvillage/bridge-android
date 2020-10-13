package com.adoptvillage.bridge.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.adoptvillage.bridge.R
import kotlinx.android.synthetic.main.activity_application_detail.*

class ApplicationDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_detail)

        tvAppDetailRecipient.text = intent.getStringExtra("rName")
        tvAppDetailLocation.text = intent.getStringExtra("rLocation")
        tvAppDetailInstituteName.text = intent.getStringExtra("rInstitute")
        tvAppDetailAmount.text = intent.getStringExtra("rAmount")

        tvPaymentOption.setOnClickListener {
            sbAmount.isChecked = !sbAmount.isChecked

            if(sbAmount.isChecked)
            {
                cvAppDetailsDonatedAmount.visibility = View.GONE
            }
            else
            {
                cvAppDetailsDonatedAmount.visibility = View.VISIBLE
            }
        }

        sbAmount.setOnCheckedChangeListener{ compoundButton, onSwitch->

            if(onSwitch)
            {
                cvAppDetailsDonatedAmount.visibility = View.GONE
            }
            else
            {
                cvAppDetailsDonatedAmount.visibility = View.VISIBLE
            }

        }


        tvAppDetailBack.setOnClickListener {
            startActivity(Intent(this, ApplicationsListActivity::class.java))
        }
    }
}