package com.example.cho6.guru2_android12.Adapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cho6.guru2_android12.R
import kotlinx.android.synthetic.main.call_ok.*

// CallOne 또는 CallAll 팝업에서 예를 눌렀을 때
class CallOk : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.call_ok)

        // 창닫기 버튼을 눌렀을 때
        close_call_ok.setOnClickListener {
            // 창닫기
            finish()
        }
    }
}