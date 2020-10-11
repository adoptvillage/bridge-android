package com.adoptvillage.bridge.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.adapters.ApplicationListAdapter
import com.adoptvillage.bridge.models.List_card_model
import kotlinx.android.synthetic.main.activity_applications_list.*

class ApplicationsListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applications_list)

        val entries = generateDummyList(20)

        rvApplicationList.adapter = ApplicationListAdapter(entries)
        rvApplicationList.layoutManager = LinearLayoutManager(this)
        rvApplicationList.setHasFixedSize(true)

        tvApplicationListBack.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

    }

    private fun generateDummyList(size: Int): List<List_card_model>
    {
        val list = ArrayList<List_card_model>()

        for(i in 0 until size)
        {
            var amt = (i+1)*100
            val item = List_card_model("Recipient ${i+1}","District ${i+1}, State ${i+1}", "College ${i+1}",amt.toString())
            list += item
        }

        return list
    }


}