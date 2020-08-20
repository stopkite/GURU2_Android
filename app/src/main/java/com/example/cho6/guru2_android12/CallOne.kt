package com.example.cho6.guru2_android12

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import kotlinx.android.synthetic.main.call_all.*
import kotlinx.android.synthetic.main.call_one.*

class CallOne : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.call_one)

        var useruid: String? = intent.getStringExtra("useruid")
        var usernickname: String? = intent.getStringExtra("usernickname")


        nickname_call_one.text=usernickname


        yes_call_one.setOnClickListener {
            finish()
            val callokpopup= Intent(this, CallOk::class.java)
            startActivity(callokpopup)
        }

        no_call_one.setOnClickListener {
            finish()
            val callfailpopup= Intent(this, CallFail::class.java)
            startActivity(callfailpopup)
        }

        close_call_one.setOnClickListener {
            finish()
        }


    }
}