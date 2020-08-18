// s3. 채팅 목록
package com.example.cho6.guru2_android12


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.remove
import android.util.Log
import android.widget.EditText
import androidx.core.view.OneShotPreDrawListener.add
import com.example.cho6.guru2_android12.Adapter.ChatLeftYou
import com.example.cho6.guru2_android12.Adapter.ChatRightMe
import com.example.cho6.guru2_android12.Adapter.UserItem
import com.example.cho6.guru2_android12.ChatRoomActivity
import com.example.cho6.guru2_android12.Model.ChatNewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_list.*
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.chat_left_you.*

class ChatListActivity : AppCompatActivity() {

    private val TAG = ChatListActivity::class.java.simpleName

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth //나의 uid 가져오기


    override fun onCreate(savedInstanceState: Bundle?) {
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
                                document.get("profileImageUrl").toString()
                            )
                        )

                }

                //document.get("uid") != myUid
                recycle_view_list.adapter = adapter // recycleview apdapter(2)

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
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