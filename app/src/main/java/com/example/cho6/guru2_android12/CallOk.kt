package com.example.cho6.guru2_android12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.call_ok.*

class CallOk : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.call_ok)

        close_call_ok.setOnClickListener {
            finish()
        }
    }
}