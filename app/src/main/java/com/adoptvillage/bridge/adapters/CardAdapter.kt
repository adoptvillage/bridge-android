package com.adoptvillage.bridge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.viewpager.widget.PagerAdapter
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.fragment.homeFragment.OnCardClicked
import com.adoptvillage.bridge.models.cardModels.CardModel
import kotlinx.android.synthetic.main.application_card.view.*

class CardAdapter(private val context: Context, private val CardModelArrayList: ArrayList<CardModel>,private var onCardClicked: OnCardClicked):
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

        val donorName = currentModel.donor
        val recipientName = currentModel.recipient
        val amount=currentModel.amount
        val moderatorName = currentModel.moderator

        view.cardDonor.text = donorName
        view.tvRecipientName.text = recipientName
        view.cardAmount.text = "$ ".toString() +amount
        view.cardModerator.text = moderatorName

        view.setOnClickListener{
            Toast.makeText(context,"$recipientName's Application", LENGTH_SHORT).show()
            onCardClicked.onCardClicked(position)
        }

        container.addView(view, position)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}