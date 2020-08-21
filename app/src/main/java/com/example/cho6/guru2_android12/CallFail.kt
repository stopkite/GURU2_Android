package com.example.cho6.guru2_android12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.call_fail.*

// CallOne 또는 CallAll 팝업에서 예를 눌렀을 때
class CallFail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.call_fail)

        // 창닫기 버튼을 눌렀을 때
        close_call_fail.setOnClickListener {
            // 창닫기
            finish()
        }
    }
}