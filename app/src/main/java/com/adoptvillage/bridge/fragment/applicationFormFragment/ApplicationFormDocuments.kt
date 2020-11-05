package com.adoptvillage.bridge.fragment.applicationFormFragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationFormActivity
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.models.applicationModels.SubmitApplicationDefaultResponse
import com.adoptvillage.bridge.models.applicationModels.SubmitApplicationModel
import com.adoptvillage.bridge.service.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_application_form_documents.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ApplicationFormDocuments : Fragment() {
    val APPLICATIONSUBMITTAG="APPLICATIONSUBMITTAG"
    val PDF1 = 0
    val PDF2 = 1
    val PDF3 = 2
    lateinit var enrollmentProofURI : Uri
    lateinit var feeStructureURI : Uri
    lateinit var bankStatementURI: Uri
    lateinit var mStorageRef:StorageReference
    lateinit var mAuth:FirebaseAuth
    lateinit var feeStructureDownloadableUrl:Uri
    lateinit var enrollmentLetterDownloadableUrl:Uri
    lateinit var bankStatementDownloadableUrl:Uri
    var enrollmentLetterUploaded=false
    var feeStructureUploaded=false
    var bankStatementUploaded=false
    var enrollmentLetterSelected=false
    var feeStructureSelected=false
    var bankStatementSelected=false

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
        return inflater.inflate(R.layout.fragment_application_form_documents, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mStorageRef = FirebaseStorage.getInstance().reference
        mAuth= FirebaseAuth.getInstance()
        btnAppUDUpload.isEnabled=false
        btnAppUDSubmit.isEnabled=false
        tvDocumentBackSetOnClickListener()
        tvProofSetOnClickListener()
        tvFeeStructureSetOnClickListener()
        tvBankStatementSetOnClickListener()
        btnAppUDUploadSetOnClickListener()
        btnAppUDSubmitSetOnClickListener()
        Log.i("test",ApplicationFormActivity.studentFirstName+ApplicationFormActivity.studentLastName+ApplicationFormActivity.studentAadhaarNumber+ApplicationFormActivity.studentContactNumber)

    }

    private fun btnAppUDSubmitSetOnClickListener() {
        btnAppUDSubmit.setOnClickListener {
            Log.i(APPLICATIONSUBMITTAG, "button click")
            if (enrollmentLetterUploaded && feeStructureUploaded && bankStatementUploaded) {
                pbAppUDSubmit.visibility=View.VISIBLE
                val obj= SubmitApplicationModel(
                    applicantFirstName = ApplicationFormActivity.studentFirstName,
                    applicantLastName = ApplicationFormActivity.studentLastName,
                    contactNumber = ApplicationFormActivity.studentContactNumber,
                    aadhaarNumber = ApplicationFormActivity.studentAadhaarNumber,
                    state = ApplicationFormActivity.state,
                    district = ApplicationFormActivity.district,
                    subDistrict = ApplicationFormActivity.subDistrict,
                    area = ApplicationFormActivity.village,
                    instituteName = ApplicationFormActivity.instituteName,
                    instituteState = ApplicationFormActivity.instituteState,
                    instituteDistrict = ApplicationFormActivity.instituteDistrict,
                    instituteType =ApplicationFormActivity.instituteType,
                    institutionAffiliationCode = ApplicationFormActivity.instituteAffCode,
                    courseName = ApplicationFormActivity.instituteCourse,
                    yearOrSemester = ApplicationFormActivity.instituteSemester,
                    amount = ApplicationFormActivity.instituteFeesAmount,
                    offerLetter = enrollmentLetterDownloadableUrl.toString(),
                    feeStructure = feeStructureDownloadableUrl.toString(),
                    bankStatement = bankStatementDownloadableUrl.toString(),
                    description = ApplicationFormActivity.studentPurpose
                )
                Log.i("test",ApplicationFormActivity.studentFirstName + ApplicationFormActivity.studentLastName+ApplicationFormActivity.studentContactNumber+ApplicationFormActivity.studentAadhaarNumber+ApplicationFormActivity.state+ApplicationFormActivity.instituteFeesAmount)
                RetrofitClient.instance.applicationService.submitApplication(obj)
                    .enqueue(object : Callback<SubmitApplicationDefaultResponse> {
                        override fun onResponse(
                            call: Call<SubmitApplicationDefaultResponse>,
                            response: Response<SubmitApplicationDefaultResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.i(APPLICATIONSUBMITTAG, "success")
                                toastMaker(response.body()?.message)
                                toDashboardActivity()
                            } else {
                                val jObjError = JSONObject(response.errorBody()!!.string())
                                Log.i(APPLICATIONSUBMITTAG, response.toString())
                                Log.i(APPLICATIONSUBMITTAG, jObjError.getString("message"))
                                toastMaker(jObjError.getString("message"))
                                toDashboardActivity()
                            }
                        }

                        override fun onFailure(call: Call<SubmitApplicationDefaultResponse>, t: Throwable) {
                            Log.i(APPLICATIONSUBMITTAG, "error" + t.message)
                            toastMaker("Failed to Submit Application - " + t.message)
                            pbAppUDSubmit.visibility=View.INVISIBLE
                        }
                    })
            }else{
                Log.i(APPLICATIONSUBMITTAG, "no document")
                toastMaker("document not uploaded")
            }
        }
    }

    private fun toDashboardActivity() {
        pbAppUDSubmit.visibility=View.INVISIBLE
        val intent = Intent(context, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun btnAppUDUploadSetOnClickListener() {
        btnAppUDUpload.setOnClickListener {
            btnAppUDUpload.isEnabled=false
            pbAppUDUpload.visibility=View.VISIBLE
            uploadEnrollmentLetter()
        }
    }

    private fun uploadEnrollmentLetter() {
        val mDocsRef = mStorageRef.child(mAuth.currentUser!!.uid).child("docs/offer_letter.pdf")
        mDocsRef.putFile(enrollmentProofURI).continueWithTask{task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mDocsRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                Log.i("test",it.result.toString())
                enrollmentLetterDownloadableUrl=it.result!!
                enrollmentLetterUploaded=true
                uploadFeesStructure()
            }
            else{
                Log.i("test","upload failed")
            }
        }
    }

    private fun uploadFeesStructure() {
        val mDocsRef = mStorageRef.child(mAuth.currentUser!!.uid).child("docs/fee_structure.pdf")
        mDocsRef.putFile(feeStructureURI).continueWithTask{task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mDocsRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                Log.i("test",it.result.toString())
                feeStructureDownloadableUrl=it.result!!
                feeStructureUploaded=true
                uploadBankStatement()
            }
            else{
                Log.i("test","upload failed")
            }
        }
    }
    private fun toastMaker(message: String?) {

        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

    }
    private fun uploadBankStatement() {
        val mDocsRef = mStorageRef.child(mAuth.currentUser!!.uid).child("docs/bank_statement.pdf")
        mDocsRef.putFile(bankStatementURI).continueWithTask{task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mDocsRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                Log.i("test",it.result.toString())
                bankStatementDownloadableUrl=it.result!!
                bankStatementUploaded=true
                btnAppUDUpload.isEnabled=true
                btnAppUDSubmit.isEnabled=true
                pbAppUDUpload.visibility=View.INVISIBLE
                btnAppUDUpload.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
                toastMaker("uploaded documents")
            }
            else{
                Log.i("test","upload failed")
            }
        }
    }

    private fun tvBankStatementSetOnClickListener() {
        tvBankStatement.setOnClickListener{
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF3)
        }
    }

    private fun tvFeeStructureSetOnClickListener() {
        tvFeeStructure.setOnClickListener{
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF2)
        }
    }

    private fun tvProofSetOnClickListener() {
        tvProof.setOnClickListener {
            val intent = Intent()
            intent.type="application/pdf"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"),PDF1)

        }
    }

    private fun tvDocumentBackSetOnClickListener() {
        tvDocumentBack.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.clAFAFullScreen, ApplicationFormInstituteDetails())?.commit()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if(requestCode == PDF1)
            {
                if (data != null)
                {
                    enrollmentProofURI = data.data!!
                    enrollmentLetterSelected=true
                    tvProof.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
                    if (enrollmentLetterSelected && feeStructureSelected && bankStatementSelected){
                        btnAppUDUpload.isEnabled=true
                    }
                }
            }
            else if (requestCode == PDF2)
            {
                if(data!=null)
                {
                    feeStructureURI = data.data!!
                    feeStructureSelected=true
                    tvFeeStructure.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
                    if (enrollmentLetterSelected && feeStructureSelected && bankStatementSelected){
                        btnAppUDUpload.isEnabled=true
                    }
                }
            }
            else if(requestCode ==PDF3 )
            {
                if(data!=null)
                {
                    bankStatementURI = data.data!!
                    bankStatementSelected=true
                    tvBankStatement.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
                    if (enrollmentLetterSelected && feeStructureSelected && bankStatementSelected){
                        btnAppUDUpload.isEnabled=true
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}

