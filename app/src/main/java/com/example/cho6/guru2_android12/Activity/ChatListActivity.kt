//s4.채팅목록 화면
package com.example.cho6.guru2_android12.Activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cho6.guru2_android12.Adapter.UserItem
import com.example.cho6.guru2_android12.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_list.*

class ChatListActivity : AppCompatActivity() {

    private val TAG = ChatListActivity::class.java.simpleName

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth //나의 uid 가져오기


    override fun onCreate(savedInstanceState: Bundle?) {

        //docRequestSign().signTurnTrue(false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list) // 's3.채팅 목록'을 위한 xml

        auth = FirebaseAuth.getInstance()

        val adapter = GroupAdapter<GroupieViewHolder>() // recycleview apdapter(1)

        val database = Firebase.database
        val myUid: String? = auth.uid
        val yourUid: String? = intent.getStringExtra("yourUid")


        // read data from a new document with a generated Id(데이터 읽기)
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    if (document.get("uid") != myUid)
                        adapter.add(
                            UserItem(
                                document.get("username").toString(),
                                document.get("uid").toString(),
                                document.get("profileImageUrl").toString() //프로필 사진 생성(5) -불러오기
                            )
                        )

                }

                //document.get("uid") != myUid
                recycle_view_list.adapter = adapter // recycleview apdapter(2)

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        //뒤로가기버튼(위드미로)
        go_to_withme_button.setOnClickListener{

            val intent = Intent(this,
                withmefriendActivity::class.java)
            startActivity(intent)
        }


        // 's4.채팅방' 화면으로 이동하기 위한 함수
        adapter.setOnItemClickListener { item, view ->

            val uid = (item as UserItem).uid
            val name = (item as UserItem).name // Useritem : Adapter -> Useritem.kt에서 받아 옴

            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.putExtra("yourUid", uid)  // 대화 상대의 uid 값을 받아준다
            intent.putExtra("name", name)    // 대화 상대의 name을 받아준다
            startActivity(intent)
        }
        recycle_view_list.adapter = adapter

    }






}

