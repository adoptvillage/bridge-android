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
        if (DashboardActivity.historyApiList.isNullOrEmpty()){
            Log.i(HISTORYFRAGTAG,"call")
            getHistoryInfo()
        }
        else{
            if (DashboardActivity.fragmentNumberSaver==2) {
                rvHistory?.adapter = HistoryListAdapter(DashboardActivity.historyApiList)
                rvHistory?.layoutManager = LinearLayoutManager(context)
                rvHistory?.setHasFixedSize(true)
            }
            setBarChart()
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
                                    DashboardActivity.historyApiList = list
                                    DashboardActivity.isHistoryAvail=1
                                    if (DashboardActivity.fragmentNumberSaver == 2) {
                                        rvHistory?.adapter = HistoryListAdapter(list)
                                        rvHistory?.layoutManager = LinearLayoutManager(context)
                                        rvHistory?.setHasFixedSize(true)
                                    }
                                    setBarChart()
                                }
                            }
                            else{
                                Log.i(HISTORYFRAGTAG,"no record")
                                val item = HistoryCardModel(
                                    "No History Found","....","....","....","...."
                                )
                                list += item
                                DashboardActivity.historyApiList=list
                                DashboardActivity.isHistoryAvail=0
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
        if (DashboardActivity.isHistoryAvail==1){
            val year=DashboardActivity.historyApiList[0].closingDate.substring(0,4)
            var amountQuarter1=0
            var amountQuarter2=0
            var amountQuarter3=0
            var amountQuarter4=0
            for (i in 0 until DashboardActivity.historyApiList.size){
                if (DashboardActivity.historyApiList[i].closingDate.substring(0,4)==year){
                    when {
                        DashboardActivity.historyApiList[i].closingDate.substring(5,7).toInt()<=3 -> {
                            amountQuarter1+=DashboardActivity.historyApiList[i].amount.toInt()
                        }
                        DashboardActivity.historyApiList[i].closingDate.substring(5,7).toInt()<=6 -> {
                            amountQuarter2+=DashboardActivity.historyApiList[i].amount.toInt()
                        }
                        DashboardActivity.historyApiList[i].closingDate.substring(5,7).toInt()<=9 -> {
                            amountQuarter3+=DashboardActivity.historyApiList[i].amount.toInt()
                        }
                        DashboardActivity.historyApiList[i].closingDate.substring(5,7).toInt()<=12 -> {
                            amountQuarter4+=DashboardActivity.historyApiList[i].amount.toInt()
                        }
                    }
                }
            }
            val entries = ArrayList<BarEntry>()
            entries.add(BarEntry(amountQuarter1.toFloat(), 0))
            entries.add(BarEntry(amountQuarter2.toFloat(), 1))
            entries.add(BarEntry(amountQuarter3.toFloat(), 2))
            entries.add(BarEntry(amountQuarter4.toFloat(), 3))
            val barDataSet = BarDataSet(entries, "Cells")
            val labels = ArrayList<String>()
            labels.add("Jan-Mar")
            labels.add("Apr-Jun")
            labels.add("Jul-sep")
            labels.add("Oct-Dec")
            val data = BarData(labels, barDataSet)
            if (DashboardActivity.fragmentNumberSaver==2) {
                historyChart?.data = data
                barDataSet.color = resources.getColor(R.color.systemBlue)
                historyChart?.animateY(2000)
            }
        }
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

