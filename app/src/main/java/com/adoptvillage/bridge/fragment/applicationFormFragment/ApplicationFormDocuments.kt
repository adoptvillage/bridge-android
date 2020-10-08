package com.adoptvillage.bridge.fragment.applicationFormFragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity
import kotlinx.android.synthetic.main.fragment_application_form_documents.*
import kotlinx.android.synthetic.main.fragment_application_form_institute_details.*


class ApplicationFormDocuments : Fragment() {

    val PDF1 = 0
    val PDF2 = 1
    val PDF3 = 2
    lateinit var EnrollmentProofURI : Uri
    lateinit var FeeStructureURI : Uri
    lateinit var BankStatementURI: Uri

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
        btnAppUDUpload.setOnClickListener {
            val intent= Intent(context, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        tvDocumentBackSetOnClickListener()



        tvProof.setOnClickListener {
            val intent = Intent()
            intent.type="application/pdf"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"),PDF1)

        }

       tvFeeStructure.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF2)
        })

        tvBankStatement.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF3)
        })

    }

    private fun tvDocumentBackSetOnClickListener() {
        tvDocumentBack.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.clAFAFullScreen, ApplicationFormInstituteDetails())?.commit()
        }
    }

//    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
//        super.startActivityForResult(intent, requestCode)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if(requestCode == PDF1)
            {
                if (data != null)
                {
                    EnrollmentProofURI = data.data!!
                    tvProof.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
                }
            }
            else if (requestCode == PDF2)
            {
                if(data!=null)
                {
                    FeeStructureURI = data.data!!
                    tvFeeStructure.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
                }
            }
            else if(requestCode ==PDF3 )
            {
                if(data!=null)
                {
                    BankStatementURI = data.data!!
                    tvBankStatement.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_check_24 , 0)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}