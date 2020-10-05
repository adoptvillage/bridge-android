package com.adoptvillage.bridge.fragment.applicationFormFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.fragment.homeFragment.LocationFragment
import kotlinx.android.synthetic.main.fragment_application_form_institute_details.*


class ApplicationFormInstituteDetails : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_application_form_institute_details,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAppIDNext.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.clAFAFullScreen, ApplicationFormDocuments())?.commit()
        }
    }

}