package com.adoptvillage.bridge.fragment.applicationListFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationsListActivity
import kotlinx.android.synthetic.main.fragment_application_detail_1.*

class ApplicationDetail1 : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.explode)
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

        ApplicationsListActivity.fragnumber=1
        displaySelectedApplicationData()
        btnAppDetail1NextSetOnClickListener()
        tvAppDetail1BackSetOnClickListener()
    }

    private fun tvAppDetail1BackSetOnClickListener() {
        tvAppDetail1Back.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fl_wrapper_applications, ApplicationsListFragment())?.commit()
        }
    }

    private fun displaySelectedApplicationData() {
        val recipientLocation=ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].state+", "+ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].district+", "+ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].subDistrict+", "+ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].area
        val recipientName=ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].applicantFirstName+" "+ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].applicantLastName
        val instituteLocation=ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].instituteState+", "+ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].instituteDistrict
        tvAppDetailInstituteName.text=ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].institute
        tvAppDetailAmount.text=ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].remainingAmount.toString()
        tvAppDetailRecipientHometown.text=recipientLocation
        tvAppDetailRecipient.text=recipientName
        tvAppDetail1InsLocation.text=instituteLocation
    }

    private fun btnAppDetail1NextSetOnClickListener()
    {
        btnAppDetail1Next.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fl_wrapper_applications, ApplicationDetail2())?.commit()
        }
    }

}