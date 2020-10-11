package com.adoptvillage.bridge.fragment.historyFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.adapters.HistoryListAdapter
import com.adoptvillage.bridge.models.HistoryCardModel
import kotlinx.android.synthetic.main.fragment_history.*


class HistoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.explode)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DashboardActivity.fragmentNumberSaver=2


        val entries = generateDummyList(20)

        rvHistory.adapter = HistoryListAdapter(entries)
        rvHistory.layoutManager = LinearLayoutManager(context)
        rvHistory.setHasFixedSize(true)
    }


    private fun generateDummyList(size: Int): List<HistoryCardModel>
    {
        val list = ArrayList<HistoryCardModel>()

        for(i in 0 until size)
        {
            val amt = (i+1)*100
            val item = HistoryCardModel("Recipient ${i+1}",
                "Donor ${i+1}",
                "Moderator ${i+1}",
                amt.toString(),
                "${i+1}/10/2020"
                )

            list += item
        }
        return list
    }
}

