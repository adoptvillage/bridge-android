package com.adoptvillage.bridge.fragment.historyFragment

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
import com.adoptvillage.bridge.models.cardModels.HistoryCardModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
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

        setBarChart()

        val entries = generateDummyList(20)

        rvHistory.adapter = HistoryListAdapter(entries)
        rvHistory.layoutManager = LinearLayoutManager(context)
        rvHistory.setHasFixedSize(true)
    }


    private fun setBarChart()
    {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(8f, 0))
        entries.add(BarEntry(2f, 1))
        entries.add(BarEntry(5f, 2))
        entries.add(BarEntry(20f, 3))
        entries.add(BarEntry(15f, 4))
        entries.add(BarEntry(19f, 5))

        val barDataSet = BarDataSet(entries, "Cells")

        val labels = ArrayList<String>()
        labels.add("18-Jan")
        labels.add("19-Jan")
        labels.add("20-Jan")
        labels.add("21-Jan")
        labels.add("22-Jan")
        labels.add("23-Jan")
        val data = BarData(labels, barDataSet)
        historyChart.data = data // set the data and list of lables into chart

//        historyChart.setDescription("Set Bar Chart Description")  // set the description


        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.color = resources.getColor(R.color.systemBlue)

        historyChart.animateY(2000)

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
                "$"+amt.toString(),
                "${i+1}/10/2020"
                )

            list += item
        }
        return list
    }
}

