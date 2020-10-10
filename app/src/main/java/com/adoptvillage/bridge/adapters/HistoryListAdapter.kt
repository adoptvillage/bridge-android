package com.adoptvillage.bridge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.models.HistoryCardModel
import kotlinx.android.synthetic.main.history_card.view.*

class HistoryListAdapter(private val entries:List<HistoryCardModel>): RecyclerView.Adapter<HistoryListAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val recipientName : TextView = itemView.tvHistoryRecipient
        val donorName : TextView = itemView.tvHistoryDonor
        val moderatorName : TextView = itemView.tvHistoryModerator
        val amountInvolved : TextView = itemView.tvHistoryAmount
        val closingDate : TextView = itemView.tvHistoryDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_card,parent,false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = entries[position]

        holder.recipientName.text = currentItem.recipient
        holder.donorName.text = currentItem.donor
        holder.moderatorName.text = currentItem.moderator
        holder.amountInvolved.text = currentItem.amount
        holder.closingDate.text = currentItem.closingDate

        holder.itemView.setOnClickListener {v: View->
            Toast.makeText(v.context,"${holder.recipientName.text}'s Application", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return entries.size
    }
}