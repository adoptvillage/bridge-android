package com.adoptvillage.bridge.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.adoptvillage.bridge.R
import java.util.Random
import com.adoptvillage.bridge.adapters.ApplicationListAdapter
import com.adoptvillage.bridge.models.List_card_model
import kotlinx.android.synthetic.main.activity_applications_list.*

class ApplicationsListActivity : AppCompatActivity(), onApplicationClicked {

    val list = ArrayList<List_card_model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applications_list)

        generateDummyList(20)

        rvApplicationList.adapter = ApplicationListAdapter(list,this)
        rvApplicationList.layoutManager = LinearLayoutManager(this)
        rvApplicationList.setHasFixedSize(true)

        tvApplicationListBack.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

    }

    private fun generateDummyList(size: Int)
    {
        for(i in 0 until size)
        {
            var amt = (i+1)*100
            val item = List_card_model("Recipient ${i+1}","District ${i+1}, State ${i+1}", "College ${i+1}","₹"+amt.toString())
            list += item
        }

    }

    override fun onApplicationItemClicked(position: Int) {
        val intent = Intent(this, ApplicationDetailActivity::class.java)
        intent.putExtra("rName",list[position].recipient)
        intent.putExtra("rAmount",list[position].amount)
        intent.putExtra("rInstitute",list[position].institution)
        intent.putExtra("rLocation",list[position].location)
        intent.putExtra("rActiveDonor","5")
        startActivity(intent)
    }


}