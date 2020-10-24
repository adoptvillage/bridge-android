package com.adoptvillage.bridge.activity

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.adapters.ChatAdapter
import com.adoptvillage.bridge.models.ChatModel
import com.adoptvillage.bridge.models.DateHeader
import com.adoptvillage.bridge.models.Message
import com.adoptvillage.bridge.utils.isSameDayAs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_chat.*
import java.io.File

class ChatActivity : AppCompatActivity(),OnClicked {
    private var flagForTypeDownload: Int=0 //IMAGE=0  PDF == 1
    private val STORAGE_PERMISSION_CODE: Int=1000
    lateinit var selectedPDFUri: Uri
    lateinit var imageDownloadableUrl: Uri
    lateinit var pdfDownloadableUrl: Uri
    lateinit var mAuth: FirebaseAuth
    lateinit var mStorageRef: StorageReference
    lateinit var selectedImageUri:Uri
    var userId="IR3WducU7oNeKg0UAAcUVERcZJy2"
    var friendId="sbh8kZQ1XWVAFiq8GyK86Hob4Rg1"
    var imageNumberId=0
    var pdfNumberId=0
    val IMAGE_CODE=0
    var PDF_CODE=1
    lateinit var currentDownloadUrl:String
    lateinit var currentId:String
    private val db: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        mStorageRef = FirebaseStorage.getInstance().reference
        mAuth= FirebaseAuth.getInstance()
        chatAdapter = ChatAdapter(list = messages, userId, this)
        rvChatActivity.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }

        listenToMessages()
        btnCASendMessageSetOnClickListener()
        btnCAAttachmentSetOnClickListener()

    }

    private fun btnCAAttachmentSetOnClickListener() {
        btnCAAttachment.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_CODE)
        }
    }

    private fun btnCASendMessageSetOnClickListener() {
        btnCASendMessage.setOnClickListener{
            etCAMessage.text?.let {  //Message written by current user.
                if (it.isNotEmpty()) {
                    sendMessage(it.toString(), "TEXT")
                    it.clear()
                }
            }
        }
    }

    private fun sendMessage(msg: String, type: String) {
        val id = getMessages(friendId).push().key //Push will generate auto new key for messages.
        checkNotNull(id) { "Cannot be null" }
        val msgMap = Message(msg, userId, id, type, imageNumberId, pdfNumberId)
        getMessages(friendId).child(id).setValue(msgMap).addOnSuccessListener {
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
        val mDocsRef = mStorageRef.child(mAuth.currentUser!!.uid).child("uploads/$pdfNumberId.pdf")
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
                sendMessage(pdfDownloadableUrl.toString(), "PDF")
            }
            else{
                Log.i("test", "upload failed")
            }
        }
    }

    private fun uploadImage() {
        val mDocsRef = mStorageRef.child(mAuth.currentUser!!.uid).child("uploads/$imageNumberId.png")
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
                sendMessage(imageDownloadableUrl.toString(), "IMAGE")
            }
            else{
                Log.i("test", "upload failed")
            }
        }
    }

    private fun listenToMessages() {
        getMessages(friendId)
            .orderByKey()
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(Message::class.java)!!
                    addMessage(message)
                    pdfNumberId = message.pdfNumberId
                    imageNumberId = message.imageNumberId
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun addMessage(msg: Message) {
        val eventBefore = messages.lastOrNull()
        if ((eventBefore != null && !eventBefore.sentAt.isSameDayAs(msg.sentAt)) || eventBefore == null) {
            messages.add(
                DateHeader(msg.sentAt, context = this)
            )
        }
        messages.add(msg)
        chatAdapter.notifyItemInserted(messages.size - 1)
        rvChatActivity.scrollToPosition(messages.size - 1)
    }

    private fun getMessages(friendId: String) = db.reference.child("messages/${getId(friendId)}")

    private fun getId(friendId: String): String {
        return if (friendId > userId)
            userId + friendId
        else
            friendId + userId
    }

    override fun onImageClicked(url: String, msgID: String) {

        val file = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS+"/$msgID.jpeg"
        )
        val path = file.absolutePath

        Log.i("test",path.toString())
        if (file.exists()) {
            Log.i("test", "Exist")
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(Uri.parse(path), "image/*")
            startActivity(intent)
        } else {
            Toast.makeText(this,"Download file to view",Toast.LENGTH_SHORT).show()
            Log.i("test", "not Exist")
        }
    }

    override fun onPdfClicked(url: String, msgID: String) {
        val file = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS+"/$msgID.pdf"
        )
        val path = file.absolutePath

        Log.i("test",path.toString())
        if (file.exists()) {
            Log.i("test", "Exist")
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(Uri.parse(path), "application/pdf")
            startActivity(intent)
        } else {
            Toast.makeText(this,"Download file to view",Toast.LENGTH_SHORT).show()
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
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$id.jpeg")

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
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$id.pdf")

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