package com.adoptvillage.bridge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.fragment.CellClickListener
import kotlinx.android.synthetic.main.view_area_listing.view.*

class AreaListingAdapter(
    private val context: Context,
    private var list: MutableList<String?>,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<AreaListingAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_area_listing, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.tvLLSAName.text=list[position]
        checkingForSavedLocationForCheckBox(position, holder)
        holderItemViewSetOnClickListener(position, holder)

    }
    fun updateList(updatedList: MutableList<String?>) {
        list = updatedList
        notifyDataSetChanged()
    }

    private fun holderItemViewSetOnClickListener(
        position: Int,
        holder: AreaListingAdapter.ViewHolder
    ) {
        holder.itemView.setOnClickListener {
            when (DashboardActivity.dataForLocationFrag) {
                1 -> {
                    DashboardActivity.state = list[position]!!
                    for (i in DashboardActivity.locationDataModel.states!!.indices){
                        if (DashboardActivity.locationDataModel.states!![i]!!.state==list[position]){
                            DashboardActivity.stateNum=i
                        }
                    }
                }
                2 -> {
                    DashboardActivity.district = list[position]!!
                    for (i in DashboardActivity.locationDataModel.states!![DashboardActivity.stateNum]!!.districts!!.indices){
                        if (DashboardActivity.locationDataModel.states!![DashboardActivity.stateNum]!!.districts!![i]!!.district==list[position]){
                            DashboardActivity.districtNum=i
                        }
                    }
                }
                3 -> {
                    DashboardActivity.subDistrict = list[position]!!
                    for (i in DashboardActivity.locationDataModel.states!![DashboardActivity.stateNum]!!.districts!![DashboardActivity.districtNum]!!.subDistricts!!.indices){
                        if (DashboardActivity.locationDataModel.states!![DashboardActivity.stateNum]!!.districts!![DashboardActivity.districtNum]!!.subDistricts!![i]!!.subDistrict==list[position]){
                            DashboardActivity.subDistrictNum=i
                        }
                    }

                }
                4 -> {
                    DashboardActivity.village = list[position]!!
                    for (i in DashboardActivity.locationDataModel.states!![DashboardActivity.stateNum]!!.districts!![DashboardActivity.districtNum]!!.subDistricts!![DashboardActivity.subDistrictNum]!!.villages!!.indices){
                        if (DashboardActivity.locationDataModel.states!![DashboardActivity.stateNum]!!.districts!![DashboardActivity.districtNum]!!.subDistricts!![DashboardActivity.subDistrictNum]!!.villages!![i]==list[position]){
                            DashboardActivity.villageNum=i
                        }
                    }
                }
            }
            cellClickListener.onCellClickListener()
        }
    }

    private fun checkingForSavedLocationForCheckBox(
        position: Int,
        holder: AreaListingAdapter.ViewHolder
    ) {
        when (DashboardActivity.dataForLocationFrag) {
            1 -> {
                if (position == DashboardActivity.stateNum) {
                    holder.itemView.cbLLSAItem.isChecked = true
                }
            }
            2 -> {
                if (position == DashboardActivity.districtNum) {
                    holder.itemView.cbLLSAItem.isChecked = true
                }
            }
            3 -> {
                if (position == DashboardActivity.subDistrictNum) {
                    holder.itemView.cbLLSAItem.isChecked = true
                }
            }
            4 -> {
                if (position == DashboardActivity.villageNum) {
                    holder.itemView.cbLLSAItem.isChecked = true
                }
            }
        }
    }
}

