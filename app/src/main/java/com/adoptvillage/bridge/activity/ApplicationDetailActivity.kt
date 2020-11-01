package com.adoptvillage.bridge.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adoptvillage.bridge.R
import kotlinx.android.synthetic.main.activity_application_detail.*

class ApplicationDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_detail)

        tvAppDetailsBackSetOnClickListener()
        displayData()
    }

    private fun displayData() {
        if (DashboardActivity.dashboardAPIResponse.applications!=null && DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)!=null) {
            val applicantName =
                DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.applicantFirstName + " " + DashboardActivity.dashboardAPIResponse.applications?.get(
                    DashboardActivity.cardPositionClicked
                )!!.applicantLastName
            val applicantHomeTown= DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.state +" "+ DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.district+" "+DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.subDistrict+" "+ DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.area
            val instituteName=DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.institute
            val instituteLocation=DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.instituteState+" "+DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.instituteDistrict
            val amountNeeded=DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.remainingAmount.toString()
            val moderatorName=DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.moderatorName
            tvApplicantName.text=applicantName
            tvApplicantHometown.text=applicantHomeTown
            tvApplicantInstitutionName.text=instituteName
            tvApplicantInstitutionLocation.text=instituteLocation
            tvApplicantAmount.text=amountNeeded
            tvApplicantModerator.text=moderatorName
        }
    }

    private fun tvAppDetailsBackSetOnClickListener()
    {
        tvAppDetailsBack.setOnClickListener {
            finish()
        }
    }
}

