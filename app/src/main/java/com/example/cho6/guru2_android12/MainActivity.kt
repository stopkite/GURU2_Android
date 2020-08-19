// s1.메인 화면
package com.example.cho6.guru2_android12

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.view.drawToBitmap
import com.example.cho6.guru2_android12.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.chat_list.view.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val TAG: String = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // 's1.메인 화면(회원가입 및 로그인 창)'을 위한 xml 사용

        auth = FirebaseAuth.getInstance()

        // 프로필 사진 등록을 위한 버튼 함수
        select_profile_photo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }


        // 회원 가입 등록을 완료하기 위한 버튼 함수
        join_button.setOnClickListener {

            // 사용자로부터 정보를 입력받기 위한 String 변수
            val email = email_area.text.toString() // 등록할 이메일 변수
            val password = password_area.text.toString() // 등록할 비밀번호 변수

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "성공") // 계정 등록 성공 여부를 알리는 Log

                        uploadImageToFirebaseStorage()

                        val uid = FirebaseAuth.getInstance().uid ?: ""  // 등록할 사용자 id
                        val user = User(uid, username.text.toString())  // 등록할 사용자이름 변수

                        // 데이터베이스에 유저 정보를 넣어줘야 한다. -> Model 파일 User.kt 생성
                        // 이 곳에서 데이터 베이스에 정보를 넣는다
                        val db = FirebaseFirestore.getInstance().collection("users")
                        db.document(uid)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d(TAG, "데이터베이스 성공") //db에 등록 성공여부를 알리는 Log
                            }
                            .addOnFailureListener {
                                Log.d(TAG, "데이터베이스 실패") //db에 등록 실패여부를 알리는 Log
                            }

                        // 's3.채팅 목록'으로 화면을 이동한다
                        val intent = Intent(this, ChatListActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {
                        // If sign in fails, display a message to the user.

                    }

                    // ...
                }

        }


        // 's2.로그인 화면' 이동 버튼
        login_button_main.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // 프로필 사진 생성(1) - 생성 조건 (?)
    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            // proceed and check what the selected image was...

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            select_photo_imageview_register.setImageBitmap(bitmap)
            select_profile_photo.alpha = 0f
            /*   val bitmapDrawable = BitmapDrawable(bitmap)
               select_profile_photo.setBackgroundDrawable(bitmapDrawable)*/
        }
    }

    // 프로필 사진 생성(2) - 파이어 스토어에 저장하기
    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {

                    saveUserToFireDatabase(it.toString())
                }
            }
    }

    // 프로필 사진 생성(3) - 파이어 스토어 users에 저장하기
    private fun saveUserToFireDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseFirestore.getInstance().collection("users")

        val user = userProfile(uid,username.text.toString(),profileImageUrl)
        ref.document(uid)
            .set(user)
            .addOnSuccessListener {

            }

    }

}

// 프로필 사진 생성(4) - 파이어베이스에 저장할 userProfile
class userProfile(val uid:String, val username: String, val profileImageUrl: String)
