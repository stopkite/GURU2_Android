package com.example.cho6.guru2_android12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.call_fail.*

class CallFail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.call_fail)

        close_call_fail.setOnClickListener {
            finish()
        }
    }
}