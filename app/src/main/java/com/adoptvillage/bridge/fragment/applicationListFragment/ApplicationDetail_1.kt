package com.adoptvillage.bridge.fragment.applicationListFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.fragment.applicationFormFragment.ApplicationFormInstituteDetails
import kotlinx.android.synthetic.main.fragment_application_detail_1.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ApplicationDetail_1 : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_application_detail_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btntvNextSetOnClickListener()
    }

    private fun btntvNextSetOnClickListener()
    {
        tvNext.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fl_wrapper_applications, ApplicationDetail_2())?.commit()
        }
    }

}