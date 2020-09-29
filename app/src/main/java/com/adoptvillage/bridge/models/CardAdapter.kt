package com.adoptvillage.bridge.Models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import com.adoptvillage.bridge.R
import kotlinx.android.synthetic.main.application_card.view.*

class CardAdapter(private val context: Context, private val CardModelArrayList: ArrayList<CardModel>):
    PagerAdapter()
{
    override fun getCount(): Int
    {
        return CardModelArrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean
    {
        return view==`object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(context).inflate(R.layout.application_card,container,false)

        val currentModel=CardModelArrayList[position]

        val appNumber = currentModel.applicationNumber
        val donorName = currentModel.donor
        val recipientName = currentModel.recipient
        val amount=currentModel.amount

        view.applicationNumber.text = appNumber
        view.cardDonor.text = donorName
        view.cardRecipient.text = recipientName
        view.cardAmount.text = "$ ".toString() +amount

        view.setOnClickListener{
            Toast.makeText(context,"Application Number $appNumber", LENGTH_SHORT).show()
        }

        container.addView(view, position)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}