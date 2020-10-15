package com.adoptvillage.bridge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.onApplicationClicked
import com.adoptvillage.bridge.models.ApplicationResponse
import com.adoptvillage.bridge.models.List_card_model
import kotlinx.android.synthetic.main.application_card.view.*
import kotlinx.android.synthetic.main.application_list_card.view.*

class ApplicationListAdapter(private var entries:MutableList<ApplicationResponse>, private var onApplicationClicked: onApplicationClicked) : RecyclerView.Adapter<ApplicationListAdapter.ViewHolder>()
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
        val location =currentItem.state+", "+currentItem.district+", "+currentItem.subDistrict+", "+currentItem.area
        holder.recipientName.text = fullName
        holder.recipientLocation.text = location
        holder.recipientInstitution.text = currentItem.institute
        holder.recipientAmount.text = currentItem.remainingAmount.toString()

        holder.itemView.setOnClickListener {

            onApplicationClicked.onApplicationItemClicked(position)
//            Toast.makeText(v.context,"${holder.recipientName.text}'s Application",Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return entries.size
    }
}