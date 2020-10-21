package com.adoptvillage.bridge.fragment.chatScreenFragment

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.attachment_upload_card.view.*
import kotlinx.android.synthetic.main.fragment_chat.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class chatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val IMAGE = 0
    val PDF = 1
    lateinit var SelectedImageUri : Uri
    lateinit var SelectedPDFUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSendMessageSetOnClickListener()
        btnAttachmentsSetOnClickListener()
        btnChatBackSetOnClickListener()

        loadMessages()
    }

    private fun loadMessages()
    {
        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(FromChat())
        adapter.add(OurChat())
        adapter.add(FromChat())
        adapter.add(FromChat())
        adapter.add(OurChat())
        adapter.add(FromChat())
        adapter.add(OurChat())
        adapter.add(FromChat())
        adapter.add(FromChat())

        rvUserChats.adapter = adapter
    }

    class FromChat: Item<GroupieViewHolder>()
    {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getLayout(): Int {
            return R.layout.others_chat_message
        }

    }

    class OurChat: Item<GroupieViewHolder>()
    {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getLayout(): Int {
            return R.layout.our_chat_message
        }

    }


    private fun btnChatBackSetOnClickListener() {
        startActivity(Intent(context, DashboardActivity::class.java))
    }

    private fun btnAttachmentsSetOnClickListener() {
        btnAttachments.setOnClickListener {
            val view = View.inflate(context,R.layout.attachment_upload_card,null)

            val builder = AlertDialog.Builder(context)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            view.btnImageSelection.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Image"),IMAGE)
            }

            view.btnPDFSelection.setOnClickListener {
                val intent = Intent()
                intent.type = "application/pdf"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == IMAGE)
            {
                if(data!=null)
                {
                    SelectedImageUri = data.data!!
                }
            }
            else
            {
                if (data!=null)
                {
                    SelectedPDFUri = data.data!!
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun btnSendMessageSetOnClickListener() {

        val messageTyped : String = etChatMessage.text.toString()

        if (messageTyped.isNotEmpty())
        {
            Toast.makeText(context,"You typed - $messageTyped", Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(context,"Enter some message", Toast.LENGTH_SHORT).show()
        }
    }

}