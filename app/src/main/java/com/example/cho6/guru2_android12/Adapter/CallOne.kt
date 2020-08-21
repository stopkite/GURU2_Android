package com.example.cho6.guru2_android12.Adapter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.example.cho6.guru2_android12.Activity.ChatListActivity
import com.example.cho6.guru2_android12.R
import kotlinx.android.synthetic.main.call_one.*

// 한사람을 위드미 했을 때 팝업
class CallOne : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.call_one)

        // 친구목록 화면으로 부터 위드미 요청을 할 대상자의 uid와 nickname을 전달 받음
        var useruid: String? = intent.getStringExtra("useruid")
        var usernickname: String? = intent.getStringExtra("usernickname")

        // 팝업 text중 닉네임 부분을 요청을 할 대상자의 닉네임으로 변경
        nickname_call_one.text=usernickname

        // 팝업에서 예를 누르면 실행
        yes_call_one.setOnClickListener {
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
        no_call_one.setOnClickListener {
            //현재 팝업 닫고
            finish()
            // 요청취소 팝업 실행 요청
            val callfailpopup= Intent(this, CallFail::class.java)
            startActivity(callfailpopup)
        }

        // 팝업에 닫기 버튼을 누르면 실행
        close_call_one.setOnClickListener {
            //팝업 닫기
            finish()
        }


    }
}