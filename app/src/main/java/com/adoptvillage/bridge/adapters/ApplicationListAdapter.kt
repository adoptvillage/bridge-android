package com.adoptvillage.bridge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.fragment.applicationListFragment.OnApplicationClicked
import com.adoptvillage.bridge.models.applicationModels.ApplicationResponse
import kotlinx.android.synthetic.main.application_list_card.view.*

class ApplicationListAdapter(private var entries:MutableList<ApplicationResponse>, private var onApplicationClicked: OnApplicationClicked) : RecyclerView.Adapter<ApplicationListAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val recipientName : TextView = itemView.tvAppListRecipient
        val recipientLocation : TextView = itemView.tvAppListLocation
        val recipientInstitution : TextView = itemView.tvAppListInstitute
        val recipientAmount : TextView = itemView.tvAppListAmount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.application_list_card,parent,false)

        return ViewHolder(itemView)
    }
    fun updateList(updatedList: MutableList<ApplicationResponse>) {
        entries = updatedList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = entries[position]
        val fullName=currentItem.applicantFirstName+" "+currentItem.applicantLastName
        var location =currentItem.state+", "+currentItem.district
        if(currentItem.subDistrict?.length !=0 )
        {
            location = location + ", " + currentItem.subDistrict

            if(currentItem.area?.length != 0)
            {
                location = location + ", " + currentItem.area
            }
        }
        holder.recipientName.text = fullName
        holder.recipientLocation.text = location
        holder.recipientInstitution.text = currentItem.institute
        val amount="₹"+currentItem.remainingAmount.toString()
        holder.recipientAmount.text = amount

        holder.itemView.setOnClickListener {

            onApplicationClicked.onApplicationItemClicked(position)
//            Toast.makeText(v.context,"${holder.recipientName.text}'s Application",Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return entries.size
    }
}