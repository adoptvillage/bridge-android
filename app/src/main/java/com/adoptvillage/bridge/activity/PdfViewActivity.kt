package com.adoptvillage.bridge.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.adoptvillage.bridge.R
import kotlinx.android.synthetic.main.activity_pdf_view.*
import java.io.File
import java.net.URI

class PdfViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)

//        setSupportActionBar(toolbar)


        supportActionBar?.apply {
            // Set toolbar title/app title
            title = "Bridge"
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
        }

//        pdfView.fromFile().load()
        val intent = getIntent()
        val filepath = intent.getStringExtra("File")
        val file = File(filepath)

        pdfView.fromFile(file).load()

        tvPDFHeaderSetOnClickListener()
    }

    private fun tvPDFHeaderSetOnClickListener()
    {
        tvPDFHeader.setOnClickListener {
//            startActivity(Intent(this, ChatActivity::class.java))
            finish()
        }
    }
}