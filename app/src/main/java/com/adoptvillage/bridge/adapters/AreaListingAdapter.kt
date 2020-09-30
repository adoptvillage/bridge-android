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

class AreaListingAdapter (private val context: Context,
                          private val list: MutableList<String?>,
                          private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<AreaListingAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_area_listing,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.tvLLSAName.text=list[position]
        checkingForSavedLocationForCheckBox(position,holder)
        holderItemViewSetOnClickListener(position,holder)

    }

    private fun holderItemViewSetOnClickListener(position: Int, holder: AreaListingAdapter.ViewHolder) {
        holder.itemView.setOnClickListener {
            when (DashboardActivity.dataForLocationFrag) {
                1 -> {
                    DashboardActivity.state = list[position]!!
                    DashboardActivity.stateNum = position
                }
                2 -> {
                    DashboardActivity.district = list[position]!!
                    DashboardActivity.districtNum = position
                }
                3 -> {
                    DashboardActivity.subDistrict = list[position]!!
                    DashboardActivity.subDistrictNum = position
                }
                4 -> {
                    DashboardActivity.village = list[position]!!
                    DashboardActivity.villageNum = position
                }
            }
            cellClickListener.onCellClickListener()
        }
    }

    private fun checkingForSavedLocationForCheckBox(position: Int, holder: AreaListingAdapter.ViewHolder) {
        when (DashboardActivity.dataForLocationFrag) {
            1 -> {
                if (position==DashboardActivity.stateNum){
                    holder.itemView.cbLLSAItem.isChecked=true
                }
            }
            2 -> {
                if (position==DashboardActivity.districtNum){
                    holder.itemView.cbLLSAItem.isChecked=true
                }
            }
            3 -> {
                if (position==DashboardActivity.subDistrictNum){
                    holder.itemView.cbLLSAItem.isChecked=true
                }
            }
            4 -> {
                if (position==DashboardActivity.villageNum){
                    holder.itemView.cbLLSAItem.isChecked=true
                }
            }
        }
    }
}

