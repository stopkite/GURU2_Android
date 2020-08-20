package com.example.cho6.guru2_android12

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.call_all.*
import kotlinx.android.synthetic.main.call_one.*

class CallAll : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.call_all)

        var usersize: String? = intent.getIntExtra("size", 0).toString()

        size.text=usersize

        yes_call_all.setOnClickListener {
            finish()
            val intent = Intent(this,ChatListActivity::class .java)
            startActivity(intent)
            val callokpopup= Intent(this, CallOk::class.java)
            startActivity(callokpopup)
        }

        no_call_all.setOnClickListener {
            finish()
            val callfailpopup= Intent(this, CallFail::class.java)
            startActivity(callfailpopup)
        }

        close_call_all.setOnClickListener {
            finish()
        }

    }
}