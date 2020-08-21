// s5.채팅방 화면
package com.example.cho6.guru2_android12.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cho6.guru2_android12.Adapter.ChatLeftYou
import com.example.cho6.guru2_android12.Adapter.ChatRightMe
import com.example.cho6.guru2_android12.Model.ChatNewModel
import com.example.cho6.guru2_android12.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoomActivity : AppCompatActivity() {

    private val TAG = ChatRoomActivity::class.java.simpleName
    private lateinit var auth: FirebaseAuth //나의 uid 가져오기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room) //'s4.채팅방 화면'을 위한 xml

        auth = FirebaseAuth.getInstance()

        val myUid: String? = auth.uid
        val yourUid: String? = intent.getStringExtra("yourUid")
        val name: String? = intent.getStringExtra("name")

        val adapter = GroupAdapter<GroupieViewHolder>()

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val database = Firebase.database
        val myRef = database.getReference("message")
        val readRef =
            database.getReference("message").child(myUid.toString()).child(yourUid.toString())

        val childEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val model: ChatNewModel? = p0.getValue(ChatNewModel::class.java)
                val msg = model?.message.toString()
                val who: String? = model?.who

                if (who == "나") {
                    adapter.add(ChatRightMe(msg))
                } else {
                    adapter.add(ChatLeftYou(msg))
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        }

        readRef.addChildEventListener(childEventListener)
        recyclerView_chat.adapter = adapter

        val myRef_list: DatabaseReference = database.getReference("message-user-List")

        // 채팅방 입력 메세지 전송 (Real time 데이터에 저장)
        // 입력 메세지가 나에게도 저장됨과 동시에 실시간으로 상대에게도 저장이 된다.
        button.setOnClickListener {

            val message: String = editText.text.toString()  // 채팅방에 작성될 메세지 변수

            val chat = ChatNewModel(
                myUid.toString(),
                yourUid.toString(),
                message,
                System.currentTimeMillis(),
                "나"
            )
            myRef.child(myUid.toString()).child(yourUid.toString()).push().setValue(chat) // 나의 uid에 대화 저장

            val chat_get = ChatNewModel(
                yourUid.toString(),
                myUid.toString(),
                message,
                System.currentTimeMillis(),
                "상대"
            )
            myRef.child(yourUid.toString()).child(myUid.toString()).push().setValue(chat_get) // 상대 uid에 대화 저장

            myRef_list.child(myUid.toString()).child(yourUid.toString()).setValue(chat) // 동시에 나의 uid에 대화 저장

            editText.setText("")    // 메세지를 보낸 뒤 초기화


        }

        // 뒤로가기 버튼(채팅 목록)
        go_to_chat_list_button.setOnClickListener{

            val intent = Intent(this,
                ChatListActivity::class.java)
            startActivity(intent)
        }
    }

}