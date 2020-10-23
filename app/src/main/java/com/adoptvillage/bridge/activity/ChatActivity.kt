package com.adoptvillage.bridge.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

class ChatActivity : AppCompatActivity() {
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
        chatAdapter = ChatAdapter(list = messages, userId)
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
            startActivityForResult(Intent.createChooser(intent, "Select Image"),IMAGE_CODE)
        }
    }

    private fun btnCASendMessageSetOnClickListener() {
        btnCASendMessage.setOnClickListener{
            etCAMessage.text?.let {  //Message written by current user.
                if (it.isNotEmpty()) {
                    sendMessage(it.toString(),"TEXT")
                    it.clear()
                }
            }
        }
    }

    private fun sendMessage(msg: String,type:String) {
        val id = getMessages(friendId).push().key //Push will generate auto new key for messages.
        checkNotNull(id) { "Cannot be null" }
        val msgMap = Message(msg, userId, id,type,imageNumberId,pdfNumberId)
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
        mDocsRef.putFile(selectedPDFUri).continueWithTask{task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mDocsRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                Log.i("test",it.result.toString())
                pdfDownloadableUrl=it.result!!
                sendMessage(pdfDownloadableUrl.toString(),"PDF")
            }
            else{
                Log.i("test","upload failed")
            }
        }
    }

    private fun uploadImage() {
        val mDocsRef = mStorageRef.child(mAuth.currentUser!!.uid).child("uploads/$imageNumberId.png")
        imageNumberId++
        mDocsRef.putFile(selectedImageUri).continueWithTask{task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mDocsRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                Log.i("test",it.result.toString())
                imageDownloadableUrl=it.result!!
                sendMessage(imageDownloadableUrl.toString(),"IMAGE")
            }
            else{
                Log.i("test","upload failed")
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
                    pdfNumberId=message.pdfNumberId
                    imageNumberId=message.imageNumberId
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

}

