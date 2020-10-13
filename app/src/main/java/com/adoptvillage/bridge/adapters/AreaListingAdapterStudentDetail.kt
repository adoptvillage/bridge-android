package com.adoptvillage.bridge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationFormActivity
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.fragment.applicationFormFragment.CellClickListener
import kotlinx.android.synthetic.main.view_area_listing.view.*

class AreaListingAdapterStudentDetail(
    private val context: Context,
    private var list: MutableList<String?>,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<AreaListingAdapterStudentDetail.ViewHolder>() {

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
        holder: AreaListingAdapterStudentDetail.ViewHolder
    ) {
        holder.itemView.setOnClickListener {
            when (ApplicationFormActivity.dataForLocationFrag) {
                1 -> {
                    ApplicationFormActivity.state = list[position]!!
                    for (i in ApplicationFormActivity.locationDataModel.states!!.indices){
                        if (ApplicationFormActivity.locationDataModel.states!![i]!!.state==list[position]){
                            ApplicationFormActivity.stateNum=i
                        }
                    }
                }
                2 -> {
                    ApplicationFormActivity.district = list[position]!!
                    for (i in ApplicationFormActivity.locationDataModel.states!![ApplicationFormActivity.stateNum]!!.districts!!.indices){
                        if (ApplicationFormActivity.locationDataModel.states!![ApplicationFormActivity.stateNum]!!.districts!![i]!!.district==list[position]){
                            ApplicationFormActivity.districtNum=i
                        }
                    }
                }
                3 -> {
                    ApplicationFormActivity.subDistrict = list[position]!!
                    for (i in ApplicationFormActivity.locationDataModel.states!![ApplicationFormActivity.stateNum]!!.districts!![ApplicationFormActivity.districtNum]!!.subDistricts!!.indices){
                        if (ApplicationFormActivity.locationDataModel.states!![ApplicationFormActivity.stateNum]!!.districts!![ApplicationFormActivity.districtNum]!!.subDistricts!![i]!!.subDistrict==list[position]){
                            ApplicationFormActivity.subDistrictNum=i
                        }
                    }

                }
                4 -> {
                    ApplicationFormActivity.village = list[position]!!
                    for (i in ApplicationFormActivity.locationDataModel.states!![ApplicationFormActivity.stateNum]!!.districts!![ApplicationFormActivity.districtNum]!!.subDistricts!![ApplicationFormActivity.subDistrictNum]!!.villages!!.indices){
                        if (ApplicationFormActivity.locationDataModel.states!![ApplicationFormActivity.stateNum]!!.districts!![ApplicationFormActivity.districtNum]!!.subDistricts!![ApplicationFormActivity.subDistrictNum]!!.villages!![i]==list[position]){
                            ApplicationFormActivity.villageNum=i
                        }
                    }
                }
            }
            cellClickListener.onCellClickListener()
        }
    }

    private fun checkingForSavedLocationForCheckBox(
        position: Int,
        holder: AreaListingAdapterStudentDetail.ViewHolder
    ) {
        when (ApplicationFormActivity.dataForLocationFrag) {
            1 -> {
                holder.itemView.cbLLSAItem.isChecked = list[position] == ApplicationFormActivity.state
            }
            2 -> {
                holder.itemView.cbLLSAItem.isChecked = list[position] == ApplicationFormActivity.district
            }
            3 -> {
                holder.itemView.cbLLSAItem.isChecked = list[position] == ApplicationFormActivity.subDistrict
            }
            4 -> {
                holder.itemView.cbLLSAItem.isChecked = list[position] == ApplicationFormActivity.village
            }
        }
    }
}

