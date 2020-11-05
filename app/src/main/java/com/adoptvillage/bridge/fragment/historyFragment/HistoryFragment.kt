package com.adoptvillage.bridge.fragment.historyFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.adapters.HistoryListAdapter
import com.adoptvillage.bridge.models.HistoryDefaultResponse
import com.adoptvillage.bridge.models.cardModels.HistoryCardModel
import com.adoptvillage.bridge.service.RetrofitClient
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.fragment_history.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HistoryFragment : Fragment() {

    private val HISTORYFRAGTAG="HISTORYFRAGTAG"

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
        loadHistoryCards()
    }

    private fun loadHistoryCards() {
        if (DashboardActivity.historyApilist.isNullOrEmpty()){
            Log.i(HISTORYFRAGTAG,"call")
            getHistoryInfo()
        }
        else{
            if (DashboardActivity.fragmentNumberSaver==2) {
                rvHistory?.adapter = HistoryListAdapter(DashboardActivity.historyApilist)
                rvHistory?.layoutManager = LinearLayoutManager(context)
                rvHistory?.setHasFixedSize(true)
            }
            Log.i(HISTORYFRAGTAG,"call")
            getHistoryInfo()
        }
    }

    private fun toastMaker(message: String?) {
        if (DashboardActivity.fragmentNumberSaver==2) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun getHistoryInfo() {
        RetrofitClient.instance.historyService.getUserHistory()
            .enqueue(object : Callback<HistoryDefaultResponse> {
                override fun onResponse(
                    call: Call<HistoryDefaultResponse>,
                    response: Response<HistoryDefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.i(HISTORYFRAGTAG,"success")
                        if (response.body()!=null) {
                            Log.i(HISTORYFRAGTAG,"body not empty")
                            val list = ArrayList<HistoryCardModel>()
                            if (response.body()!!.history!=null && response.body()!!.history?.isNotEmpty()!!) {
                                Log.i(HISTORYFRAGTAG,"record"+ response.body()!!.history?.size.toString())
                                for (i in 0 until (response.body()!!.history?.size!!)) {
                                    val item = HistoryCardModel(
                                        response.body()!!.history?.get(i)?.recipientName.toString(),
                                        response.body()!!.history?.get(i)?.donorName.toString(),
                                        response.body()!!.history?.get(i)?.moderatorName.toString(),
                                        response.body()!!.history?.get(i)?.amount.toString(),
                                        response.body()!!.history?.get(i)?.donationDate.toString()
                                    )
                                    list += item
                                    DashboardActivity.historyApilist = list
                                    if (DashboardActivity.fragmentNumberSaver == 2) {
                                        rvHistory?.adapter = HistoryListAdapter(list)
                                        rvHistory?.layoutManager = LinearLayoutManager(context)
                                        rvHistory?.setHasFixedSize(true)
                                    }
                                }
                            }
                            else{
                                Log.i(HISTORYFRAGTAG,"no record")
                                val item = HistoryCardModel(
                                    "No History Found","....","....","....","...."
                                )
                                list += item
                                DashboardActivity.historyApilist=list
                                if (DashboardActivity.fragmentNumberSaver==2) {
                                    rvHistory?.adapter = HistoryListAdapter(list)
                                    rvHistory?.layoutManager = LinearLayoutManager(context)
                                    rvHistory?.setHasFixedSize(true)
                                }
                            }
                        }
                        else{
                            Log.i(HISTORYFRAGTAG,"no record1")
                            toastMaker("No Internet / Server Down")
                        }

                    } else {
                        Log.i(HISTORYFRAGTAG,"no record2")
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(HISTORYFRAGTAG, response.toString())
                        Log.i(HISTORYFRAGTAG, jObjError.getString("message"))
                        toastMaker("Server Error"+jObjError.getString("message"))

                    }
                }

                override fun onFailure(call: Call<HistoryDefaultResponse>, t: Throwable) {
                    Log.i(HISTORYFRAGTAG, "error" + t.message)
                    toastMaker("No Internet / Server Down")
                }
            })
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

