package com.adoptvillage.bridge.activity

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.adapters.ChatAdapter
import com.adoptvillage.bridge.models.ChatModel
import com.adoptvillage.bridge.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity(),OnClicked {
    private var flagForTypeDownload: Int=0 //IMAGE=0  PDF == 1
    private val STORAGE_PERMISSION_CODE: Int=1000
    private lateinit var selectedPDFUri: Uri
    private lateinit var imageDownloadableUrl: Uri
    private lateinit var pdfDownloadableUrl: Uri
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStorageRef: StorageReference
    private lateinit var selectedImageUri:Uri
    private var userId=""
    private var donorId=""
    private var applicationId=""
    var imageNumberId=0
    var pdfNumberId=0
    private val IMAGE_CODE=0
    private var PDF_CODE=1
    private lateinit var currentDownloadUrl:String
    private lateinit var currentId:String
    private val db: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatModel>()
    private val rotateOpen:Animation by lazy { AnimationUtils.loadAnimation(
        this,
        R.anim.rotate_open_anim
    ) }
    private val rotateClose:Animation by lazy { AnimationUtils.loadAnimation(
        this,
        R.anim.rotate_close_anim
    ) }
    private val fromBottom:Animation by lazy { AnimationUtils.loadAnimation(
        this,
        R.anim.from_bottom_anim
    ) }
    private val toBottom:Animation by lazy { AnimationUtils.loadAnimation(
        this,
        R.anim.to_bottom_anim
    ) }
    private var addButtonClicked=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        if (DashboardActivity.dashboardAPIResponse.applications!=null && DashboardActivity.dashboardAPIResponse.applications?.get(
                DashboardActivity.cardPositionClicked
            )!=null) {
            val applicantName =
                DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.applicantFirstName + " " + DashboardActivity.dashboardAPIResponse.applications?.get(
                    DashboardActivity.cardPositionClicked
                )!!.applicantLastName
            tvChatHeader.text = applicantName
        }

        mAuth= FirebaseAuth.getInstance()
        donorId= DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.donorId.toString()
        userId= mAuth.currentUser!!.uid
        applicationId=DashboardActivity.dashboardAPIResponse.applications?.get(DashboardActivity.cardPositionClicked)?.id.toString()
        mStorageRef = FirebaseStorage.getInstance().reference
        chatAdapter = ChatAdapter(list = messages, userId, this)
        rvChatActivity.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }

        listenToMessages()
        btnCASendMessageSetOnClickListener()
        tvChatBackSetOnClickListener()
        btnCAAttachmentSetOnClickListener()
        tvChatHeaderSetOnClickListener()
        rvChatActivityAddOnLayoutChangeListener()

    }

    private fun rvChatActivityAddOnLayoutChangeListener() {
        rvChatActivity.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                rvChatActivity.postDelayed({
                    rvChatActivity.smoothScrollToPosition(
                        rvChatActivity.adapter!!.itemCount - 1
                    )
                }, 100)
            }
        }
    }

    private fun tvChatBackSetOnClickListener()
    {
        tvChatBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }

    private fun tvChatHeaderSetOnClickListener()
    {
        tvChatHeader.setOnClickListener {
            startActivity(Intent(this, ApplicationDetailActivity::class.java))
        }
    }

    private fun btnCAAttachmentSetOnClickListener() {

        btnCAAttachment.setOnClickListener {
            onAddButtonClicked()
        }

        btnCAAttachmentPdf.setOnClickListener {
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF_CODE)
            Toast.makeText(this, "Pdf clicked", Toast.LENGTH_SHORT).show()
            onAddButtonClicked()
        }

        btnCAAttachmentImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_CODE)
            Toast.makeText(this, "Image clicked", Toast.LENGTH_SHORT).show()
            onAddButtonClicked()
        }

    }

    private fun onAddButtonClicked() {
        fromBottom.duration=500
        toBottom.duration=300
        rotateOpen.duration=300
        rotateClose.duration=300

        if(!addButtonClicked){
            btnCAAttachmentPdf.visibility=View.VISIBLE
            btnCAAttachmentImage.visibility=View.VISIBLE
            btnCAAttachmentImage.isClickable=true
            btnCAAttachmentPdf.isClickable=true
            btnCAAttachmentImage.isFocusable=true
            btnCAAttachmentPdf.isFocusable=true
            btnCAAttachmentImage.isEnabled=true
            btnCAAttachmentPdf.isEnabled=true
            btnCAAttachmentPdf.startAnimation(fromBottom)
            btnCAAttachmentImage.startAnimation(fromBottom)
            btnCAAttachment.startAnimation(rotateOpen)
        }
        else{
            btnCAAttachmentPdf.visibility=View.INVISIBLE
            btnCAAttachmentImage.visibility=View.INVISIBLE
            btnCAAttachmentImage.isClickable=false
            btnCAAttachmentPdf.isClickable=false
            btnCAAttachmentImage.isFocusable=false
            btnCAAttachmentPdf.isFocusable=false
            btnCAAttachmentImage.isEnabled=false
            btnCAAttachmentPdf.isEnabled=false
            btnCAAttachmentPdf.startAnimation(toBottom)
            btnCAAttachmentImage.startAnimation(toBottom)
            btnCAAttachment.startAnimation(rotateClose)
        }
        addButtonClicked=!addButtonClicked
    }

    private fun btnCASendMessageSetOnClickListener() {
        btnCASendMessage.setOnClickListener{
            etCAMessage.text?.let {  //Message written by current user.
                if (it.isNotEmpty()) {
                    sendMessageToFirebase(it.toString(), "TEXT")
                    it.clear()
                }
            }
        }
    }

    private fun sendMessageToFirebase(msg: String, type: String) {
        val id = getRoomFirebaseRef().push().key //Push will generate auto new key for messages.
        checkNotNull(id) { "Cannot be null" }
        val msgMap = Message(msg, userId, id, type, imageNumberId, pdfNumberId)
        getRoomFirebaseRef().child(id).setValue(msgMap).addOnSuccessListener {
            Log.i("TAG", "sendMessage: Successful")
        }.addOnFailureListener {
            Log.i("TAG", "sendMessage: Failure")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == IMAGE_CODE) {
                if(data!=null) {
                    selectedImageUri = data.data!!
                    uploadImage()
                }
            }
            else if(requestCode==PDF_CODE){
                if(data!=null) {
                    selectedPDFUri = data.data!!
                    uploadPDF()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadPDF() {
        val mDocsRef = mStorageRef.child(getRoomId()).child("uploads/$pdfNumberId.pdf")
        pdfNumberId++
        mDocsRef.putFile(selectedPDFUri).continueWithTask{ task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mDocsRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                Log.i("test", it.result.toString())
                pdfDownloadableUrl=it.result!!
                sendMessageToFirebase(pdfDownloadableUrl.toString(), "PDF")
            }
            else{
                Log.i("test", "upload failed")
            }
        }
    }

    private fun uploadImage() {
        val mDocsRef = mStorageRef.child(getRoomId()).child("uploads/$imageNumberId.png")
        imageNumberId++
        mDocsRef.putFile(selectedImageUri).continueWithTask{ task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mDocsRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                Log.i("test", it.result.toString())
                imageDownloadableUrl=it.result!!
                sendMessageToFirebase(imageDownloadableUrl.toString(), "IMAGE")
            }
            else{
                Log.i("test", "upload failed")
            }
        }
    }

    private fun listenToMessages() {
        getRoomFirebaseRef()
            .orderByKey()
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(Message::class.java)!!
                    addMessageToRecyclerView(message)
                    pdfNumberId = message.pdfNumberId
                    imageNumberId = message.imageNumberId
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun addMessageToRecyclerView(msg: Message) {
        messages.add(msg)
        chatAdapter.notifyItemInserted(messages.size - 1)
        rvChatActivity.smoothScrollToPosition(chatAdapter.itemCount - 1)
    }

    private fun getRoomFirebaseRef() = db.reference.child("messages/${getRoomId()}")

    private fun getRoomId(): String {
        return applicationId+donorId
    }

    override fun onImageClicked(url: String, msgID: String) {

        val file = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS + "/Bridge/$msgID.jpeg"
        )
        val path = file.absolutePath
        if (file.exists()) {
            val intent = Intent(this, ImageViewActivity::class.java)
            intent.putExtra("ImagePath", path)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Download file to view", Toast.LENGTH_SHORT).show()
            Log.i("test", "not Exist")
        }
    }

    override fun onPdfClicked(url: String, msgID: String) {
        val file = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS + "/Bridge/$msgID.pdf"
        )
        val path = file.absolutePath
        Log.i("test", path.toString())
        if (file.exists()) {
            val intent = Intent(this, PdfViewActivity::class.java)
            intent.putExtra("File", path)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Download file to view", Toast.LENGTH_SHORT).show()
            Log.i("test", "not Exist")
        }
    }

    override fun onImageDownloadClicked(url: String, msgID: String) {
        currentDownloadUrl=url
        currentId=msgID
        flagForTypeDownload=0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE
                )
            }
            else{
                startDownload()
            }
        }
        else{
            startDownload()
        }
    }

    override fun onPdfDownloadClicked(url: String, msgID: String) {
        currentDownloadUrl=url
        currentId=msgID
        flagForTypeDownload=1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE
                    )
            }
            else{
                startDownload()
            }
        }
        else{
            startDownload()
        }
    }

    private fun startDownload() {
        val url=currentDownloadUrl
        val id=currentId
        if (flagForTypeDownload==0) {
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle("Download Image - Bridge")
            request.setDescription("File is downloading")
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Bridge/$id.jpeg")

            val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        }
        else{
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle("Download pdf - Bridge")
            request.setDescription("File is downloading")
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Bridge/$id.pdf")

            val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
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
}

interface OnClicked
{
    fun onImageClicked(url: String, msgID: String)

    fun onPdfClicked(url: String, msgID: String)

    fun onImageDownloadClicked(url: String, msgID: String)

    fun onPdfDownloadClicked(url: String, msgID: String)
}