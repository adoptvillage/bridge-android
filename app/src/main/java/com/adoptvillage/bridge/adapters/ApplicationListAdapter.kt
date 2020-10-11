package com.adoptvillage.bridge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.models.List_card_model
import kotlinx.android.synthetic.main.application_card.view.*
import kotlinx.android.synthetic.main.application_list_card.view.*

class ApplicationListAdapter(private val entries:List<List_card_model>) : RecyclerView.Adapter<ApplicationListAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val recipientName : TextView = itemView.tvAppListRecipient
        val recipientlocation : TextView = itemView.tvAppListLocation
        val recipientInstitution : TextView = itemView.tvAppListInstitute
        val recipientAmount : TextView = itemView.tvAppListAmount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.application_list_card,parent,false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = entries[position]

        holder.recipientName.text = currentItem.recipient
        holder.recipientlocation.text = currentItem.location
        holder.recipientInstitution.text = currentItem.institution
        holder.recipientAmount.text = currentItem.amount

        holder.itemView.setOnClickListener {v: View->
            Toast.makeText(v.context,"${holder.recipientName.text}'s Application",Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return entries.size
    }
}