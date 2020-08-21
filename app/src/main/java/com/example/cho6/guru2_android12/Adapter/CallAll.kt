package com.example.cho6.guru2_android12.Adapter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cho6.guru2_android12.Activity.ChatListActivity
import com.example.cho6.guru2_android12.R
import kotlinx.android.synthetic.main.call_all.*

// 친구목록에 있는 모든사람에게 위드미 요청을 보냈을 때
class CallAll : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.call_all)

        // 300m 안에 있는 사람들을 수를 받아오기
        var usersize: String? = intent.getIntExtra("size", 0).toString()

        // 팝업창에 있는 사람수 text를 바꾸기
        size.text=usersize

        // // 팝업에서 예를 누르면 실행
        yes_call_all.setOnClickListener {
            //현재 팝업 닫고
            finish()
            // 채팅 리스트 화면으로 전환
            val intent = Intent(this,
                ChatListActivity::class .java)
            startActivity(intent)
            // 요청완료 팝업 실행 요청
            val callokpopup= Intent(this, CallOk::class.java)
            startActivity(callokpopup)
        }

        // 팝업에서 아니오를 누르면 실행
        no_call_all.setOnClickListener {
            //현재 팝업 닫고
            finish()
            // 요청취소 팝업 실행 요청
            val callfailpopup= Intent(this, CallFail::class.java)
            startActivity(callfailpopup)
        }

        // 팝업에 닫기 버튼을 누르면 실행
        close_call_all.setOnClickListener {
            //팝업 닫기
            finish()
        }

    }
}