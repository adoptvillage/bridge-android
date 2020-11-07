package com.adoptvillage.bridge.activity

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.adoptvillage.bridge.R
import kotlinx.android.synthetic.main.activity_application_detail.*

class ApplicationDetailActivity : AppCompatActivity() {
    private var isBankStatementDownloaded=false
    private var pathBankStatement=""
    private var isFeesStrutureDownloaded=false
    private var pathFeesStructure=""
    private var isEnrollmentProofDownloaded=false
    private var pathEnrollmentProof=""
    private var applicantName: String=""
    private var downloadFileNumber=0  // 1-> enrollment    2-> fee   3->  bank statement
    private val STORAGE_PERMISSION_CODE: Int=1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_detail)

        tvAppDetailsBackSetOnClickListener()
        displayData()
        tvDownloadEnrollmentProofSetOnClickListener()
        tvDownloadFeeStructureSetOnClickListener()
        tvDownloadBankStatementSetOnClickListener()
        checkFileIsDownloadedOrNot()
    }

    private fun checkFileIsDownloadedOrNot() {
        val enrollmentProofFileName="Enrollment_Proof_$applicantName"
        val feesStructureFileName="Fees_Structure_$applicantName"
        val bankStatementFileName="Bank_Statement_$applicantName"
        val fileEnrollmentProof = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS + "/Bridge/uploads/$enrollmentProofFileName.pdf"
        )
        pathEnrollmentProof = fileEnrollmentProof.absolutePath
        isEnrollmentProofDownloaded = fileEnrollmentProof.exists()

        val fileFeesStructure = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS + "/Bridge/uploads/$feesStructureFileName.pdf"
        )
        pathFeesStructure = fileFeesStructure.absolutePath
        isFeesStrutureDownloaded = fileFeesStructure.exists()

        val fileBankStatement = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS + "/Bridge/uploads/$bankStatementFileName.pdf"
        )
        pathBankStatement = fileBankStatement.absolutePath
        isBankStatementDownloaded = fileBankStatement.exists()
        if (isEnrollmentProofDownloaded){
            tvDownloadEnrollmentProof.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
        }
        if (isBankStatementDownloaded){
            tvDownloadBankStatement.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
        }
        if (isFeesStrutureDownloaded){
            tvDownloadFeeStructure.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
        }
    }

    private fun tvDownloadBankStatementSetOnClickListener() {
        tvDownloadBankStatement.setOnClickListener {
            downloadFileNumber=3
            if (isBankStatementDownloaded){
                val bankStatementFileName="Bank_Statement_$applicantName"
                val fileBankStatement = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS + "/Bridge/uploads/$bankStatementFileName.pdf"
                )
                pathBankStatement = fileBankStatement.absolutePath
                if (fileBankStatement.exists()) {
                    val intent = Intent(this, PdfViewActivity::class.java)
                    intent.putExtra("File", pathBankStatement)
                    startActivity(intent)
                } else {
                    Toast.makeText(this,"Download again",Toast.LENGTH_SHORT).show()
                    isBankStatementDownloaded=false
                    tvDownloadBankStatement.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_cloud_download_24 , 0)
                }

            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION_CODE
                        )
                    } else {
                        startDownload()
                    }
                } else {
                    startDownload()
                }
            }
        }
    }

    private fun tvDownloadFeeStructureSetOnClickListener() {
        tvDownloadFeeStructure.setOnClickListener {
            downloadFileNumber=2
            if (isFeesStrutureDownloaded){
                val feesStructureFileName="Fees_Structure_$applicantName"
                val fileFeesStructure = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS + "/Bridge/uploads/$feesStructureFileName.pdf"
                )
                pathFeesStructure = fileFeesStructure.absolutePath
                if (fileFeesStructure.exists()) {
                    val intent = Intent(this, PdfViewActivity::class.java)
                    intent.putExtra("File", pathFeesStructure)
                    startActivity(intent)
                } else {
                    Toast.makeText(this,"Download again",Toast.LENGTH_SHORT).show()
                    isFeesStrutureDownloaded=false
                    tvDownloadFeeStructure.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_cloud_download_24 , 0)
                }
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION_CODE
                        )
                    } else {
                        startDownload()
                    }
                } else {
                    startDownload()
                }
            }
        }
    }

    private fun tvDownloadEnrollmentProofSetOnClickListener() {
        tvDownloadEnrollmentProof.setOnClickListener {
            downloadFileNumber=1
            if (isEnrollmentProofDownloaded){
                val enrollmentProofFileName="Enrollment_Proof_$applicantName"
                val fileEnrollmentProof = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS + "/Bridge/uploads/$enrollmentProofFileName.pdf"
                )
                pathEnrollmentProof = fileEnrollmentProof.absolutePath
                if (fileEnrollmentProof.exists()) {
                    val intent = Intent(this, PdfViewActivity::class.java)
                    intent.putExtra("File", pathEnrollmentProof)
                    startActivity(intent)
                } else {
                    Toast.makeText(this,"Download again",Toast.LENGTH_SHORT).show()
                    isEnrollmentProofDownloaded=false
                    tvDownloadEnrollmentProof.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_cloud_download_24 , 0)
                }
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION_CODE
                        )
                    } else {
                        startDownload()
                    }
                } else {
                    startDownload()
                }
            }
        }
    }
    private fun startDownload() {
        var name=""
        var url=""
        when (downloadFileNumber) {
            1 -> {
                url=
                    DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.offerLetter.toString()
                name="Enrollment_Proof_$applicantName"
            }
            2 -> {
                url=
                    DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.feeStructure.toString()
                name="Fees_Structure_$applicantName"
            }
            3 -> {
                url=
                    DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.bankStatement.toString()
                name="Bank_Statement_$applicantName"
            }
        }
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Downloading $name")
        request.setDescription("File is downloading")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Bridge/uploads/$name.pdf")

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
        when (downloadFileNumber) {
            1 -> {
                isEnrollmentProofDownloaded=true
                tvDownloadEnrollmentProof.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
            }
            2 -> {
                isFeesStrutureDownloaded=true
                tvDownloadFeeStructure.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
            }
            3 -> {
                isBankStatementDownloaded=true
                tvDownloadBankStatement.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownload()
                } else {
                    Toast.makeText(this, "PERMISSION DENIED!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun displayData() {
        if (DashboardActivity.dashboardAPIResponse.applications!=null && DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)!=null) {
            applicantName =
                DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.applicantFirstName + " " + DashboardActivity.dashboardAPIResponse.applications?.get(
                    DashboardActivity.cardPositionClicked
                )!!.applicantLastName
            val applicantHomeTown= DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.state +" "+ DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.district+" "+DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.subDistrict+" "+ DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.area
            val instituteName=DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.institute
            val instituteLocation=DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.instituteState+" "+DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.instituteDistrict
            val amountNeeded=DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.donatingAmount.toString()
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

