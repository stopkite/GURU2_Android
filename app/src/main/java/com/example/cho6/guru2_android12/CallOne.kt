package com.example.cho6.guru2_android12

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.call_all.*
import kotlinx.android.synthetic.main.call_one.*

class CallOne : AppCompatActivity() {

    private val CalloneViewModel: CalloneViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.call_one)

        var useruid: String? = intent.getStringExtra("useruid")
        var usernickname: String? = intent.getStringExtra("usernickname")


        nickname_call_one.text=usernickname


        yes_call_one.setOnClickListener {
            finish()
            val intent = Intent(this,ChatListActivity::class .java)
            startActivity(intent)
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

class CalloneViewModel : ViewModel(){
    val friendLiveData = MutableLiveData<List<Friends>>()
    val db = Firebase.firestore

    private val data = arrayListOf<Friends>()

    // 상대방의 요청받기 ture로 바꿔주기
    fun callOther(otherUid:String){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            //=> 상대방의 요청받기 true로 바꿔주기
            val agreeplease = hashMapOf("request" to true)
            db.collection("users").document(otherUid)
                .set(agreeplease, SetOptions.merge())
            //=> 상대방의 요청한 사람 데이터에 내 데이터 저장하기
            val

        }

    }

    // 상대방에게 수락이 왔을 때
    fun otherAgree(){
        //=> 모든 boolean값 초기화
        //채팅방으로 이동

    }

    // 상대방에게 거절이 왔을 때
    fun otherDeagree(){
        //=> 모든 boolean값 초기화
        //=> 거절되었다는 팝업?으로 이동

    }
}