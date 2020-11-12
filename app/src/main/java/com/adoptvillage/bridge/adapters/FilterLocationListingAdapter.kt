package com.adoptvillage.bridge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationsListActivity
import com.adoptvillage.bridge.fragment.applicationListFragment.CellClickListener
import kotlinx.android.synthetic.main.view_area_listing.view.*


class FilterLocationListingAdapter(private val context: Context,
                                   private var list: MutableList<String?>,
                                   private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<FilterLocationListingAdapter.ViewHolder>() {

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
        holder: FilterLocationListingAdapter.ViewHolder
    ) {
        holder.itemView.setOnClickListener {
            when (ApplicationsListActivity.dataForLocationFrag) {
                1 -> {
                    ApplicationsListActivity.state = list[position]!!
                    for (i in ApplicationsListActivity.locationDataModel.states!!.indices){
                        if (ApplicationsListActivity.locationDataModel.states!![i]!!.state==list[position]){
                            ApplicationsListActivity.stateNum=i
                        }
                    }
                }
                2 -> {
                    ApplicationsListActivity.district = list[position]!!
                    for (i in ApplicationsListActivity.locationDataModel.states!![ApplicationsListActivity.stateNum]!!.districts!!.indices){
                        if (ApplicationsListActivity.locationDataModel.states!![ApplicationsListActivity.stateNum]!!.districts!![i]!!.district==list[position]){
                            ApplicationsListActivity.districtNum=i
                        }
                    }
                }
                3 -> {
                    ApplicationsListActivity.subDistrict = list[position]!!
                    for (i in ApplicationsListActivity.locationDataModel.states!![ApplicationsListActivity.stateNum]!!.districts!![ApplicationsListActivity.districtNum]!!.subDistricts!!.indices){
                        if (ApplicationsListActivity.locationDataModel.states!![ApplicationsListActivity.stateNum]!!.districts!![ApplicationsListActivity.districtNum]!!.subDistricts!![i]!!.subDistrict==list[position]){
                            ApplicationsListActivity.subDistrictNum=i
                        }
                    }

                }
                4 -> {
                    ApplicationsListActivity.village = list[position]!!
                    for (i in ApplicationsListActivity.locationDataModel.states!![ApplicationsListActivity.stateNum]!!.districts!![ApplicationsListActivity.districtNum]!!.subDistricts!![ApplicationsListActivity.subDistrictNum]!!.villages!!.indices){
                        if (ApplicationsListActivity.locationDataModel.states!![ApplicationsListActivity.stateNum]!!.districts!![ApplicationsListActivity.districtNum]!!.subDistricts!![ApplicationsListActivity.subDistrictNum]!!.villages!![i]==list[position]){
                            ApplicationsListActivity.villageNum=i
                        }
                    }
                }
            }
            cellClickListener.onCellClickListener()
        }
    }

    private fun checkingForSavedLocationForCheckBox(
        position: Int,
        holder: FilterLocationListingAdapter.ViewHolder
    ) {
        when (ApplicationsListActivity.dataForLocationFrag) {
            1 -> {
                holder.itemView.cbLLSAItem.isChecked = list[position] == ApplicationsListActivity.state
            }
            2 -> {
                holder.itemView.cbLLSAItem.isChecked = list[position] == ApplicationsListActivity.district
            }
            3 -> {
                holder.itemView.cbLLSAItem.isChecked = list[position] == ApplicationsListActivity.subDistrict
            }
            4 -> {
                holder.itemView.cbLLSAItem.isChecked = list[position] == ApplicationsListActivity.village
            }
        }
    }
}

