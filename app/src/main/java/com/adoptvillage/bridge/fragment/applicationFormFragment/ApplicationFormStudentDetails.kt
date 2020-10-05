package com.adoptvillage.bridge.fragment.applicationFormFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adoptvillage.bridge.R
import kotlinx.android.synthetic.main.fragment_application_form_student_details.*


class ApplicationFormStudentDetails : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_application_form_student_details,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAppSDNext.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.clAFAFullScreen, ApplicationFormInstituteDetails())?.commit()
        }
    }

}