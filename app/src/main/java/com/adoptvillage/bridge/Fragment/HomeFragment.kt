package com.adoptvillage.bridge.Fragment

import android.app.ActionBar
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.adoptvillage.bridge.Models.CardAdapter
import com.adoptvillage.bridge.Models.CardModel
import com.adoptvillage.bridge.R
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {


    private lateinit var CardModelList: ArrayList<CardModel>
    private lateinit var cardAdapter: CardAdapter


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadCards()
    }

    private fun loadCards()
    {
        CardModelList = ArrayList()

        CardModelList.add(CardModel("101","Abhi","Vatsal","300"))
        CardModelList.add(CardModel("102","Ankit","Abhishek","500"))
        CardModelList.add(CardModel("103","Raju","Rasmeet","1000"))
        CardModelList.add(CardModel("104","Humraz","John Doe","400"))

        cardAdapter = activity?.let { CardAdapter(it,CardModelList) }!!

        slideView.adapter = cardAdapter

        slideView.setPadding(20,10,20,10)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


}