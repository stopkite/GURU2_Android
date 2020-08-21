package com.example.cho6.guru2_android12

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cho6.guru2_android12.databinding.ActivityFriendListBinding
import com.example.cho6.guru2_android12.databinding.ItemFriendsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_friend_list.*
import java.lang.Math.pow
import kotlin.math.sqrt

// 친구목록
class friendList : AppCompatActivity() {

    private lateinit var binding: ActivityFriendListBinding

    private val friendListViewModel: FriendListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 인텐트에서 위도와 경도 받아오기
        var latitude: Double? = intent.getDoubleExtra("latitude", 1.0)      // 위도
        var longitude: Double? = intent.getDoubleExtra("longitute", 1.0)    //경도

        // 리사이클러뷰 바인딩
        binding = ActivityFriendListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        friendListViewModel.addMyLocation(latitude!!, longitude!!)  // 현재 사용자의 위치 파이어베이스에 업로드
        friendListViewModel.findFriend(latitude!!, longitude!!)     // 300m안에 있는 사람을 찾아서 친구목록에 추가

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = FriendListAdapter(emptyList())

        // 관찰 UI 업데이트
        friendListViewModel.friendLiveData.observe(this, Observer {
            (binding.recyclerView.adapter as FriendListAdapter).setData(it as ArrayList<Friends>)
        })

        // 모든 사람 위드미하기 버튼을 눌렀을 때
        callAllButton.setOnClickListener {
            // CallAll 팝업창으로 화면 전환
            val callallpopup= Intent(this, CallAll::class.java)
            callallpopup.apply {
                this.putExtra("size", friendListViewModel.getData().size)   // 300m안에 있는 사람 수 전달
            }
            startActivity(callallpopup)
        }
    }
}

// Friends 객체
data class Friends(
    val userid: String,     // uid
    val pic: String,        // 프로필 사진
    val nickname: String,   // 닉네임
    var distance: Int       // 현재 사용자와의 거리
)

// 리사이클러 뷰 어댑터
class FriendListAdapter(private var myDataset: List<Friends>) :
    RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder>() {

    class FriendListViewHolder(val binding: ItemFriendsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendListAdapter.FriendListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friends, parent, false)
        return FriendListViewHolder(ItemFriendsBinding.bind(view))
    }

    override fun onBindViewHolder(holder: FriendListViewHolder, position: Int) {
        holder.binding.nickname.text = myDataset[position].nickname
        holder.binding.distance.text = myDataset[position].distance.toString()
        Picasso.get().load(myDataset[position].pic).into(holder.binding.frienditempic)

        // 위드미 버튼을 눌렀을 때
        holder.binding.withmeone.setOnClickListener {
            val startPopup: ImageButton = holder.binding.withmeone  // 위드미버튼
            val context: Context = startPopup.getContext()
            val pos: Int = holder.getAdapterPosition()  // 누른 버튼이 친구목록 중 몇번째 사람인지 반환(클릭위치반환)

            // CallOne팝업 띄우기
            val callonepopup= Intent(context, CallOne::class.java)
            val bundle = Bundle()
            bundle.putString("usernickname", myDataset[pos].nickname)
            bundle.putString("useruid", myDataset[pos].userid)
            callonepopup.putExtras(bundle)
            context.startActivity(callonepopup)
        }


    }

    override fun getItemCount() = myDataset.size

    // LiveData 사용을 위한 함수(데이터 업데이트)
    fun setData(newData: ArrayList<Friends>) {
        myDataset = newData
        notifyDataSetChanged()
    }
}

// 정보 수정에 대한 모든 것의 클래스
class FriendListViewModel : ViewModel() {
    val friendLiveData = MutableLiveData<List<Friends>>()
    val db = Firebase.firestore
    private val data = arrayListOf<Friends>()
    val user = FirebaseAuth.getInstance().currentUser   // 현재 사용자


    // 데이터 정보 가져오기
    fun getData():ArrayList<Friends>{
        friendLiveData.value = data
        return data
    }


    // 현재 사용자의 위치를 데이터베이스에 저장하기 위한 함수
    fun addMyLocation(myLatitude: Double, myLongitude: Double) {
        if (user != null) {
            val testla = hashMapOf("latitude" to myLatitude)    //위도
            val testlo = hashMapOf("longitude" to myLongitude)  //경도

            // 파이어베이스에 위도와 경도값 저장
            db.collection("users").document(user.uid)
                .set(testla, SetOptions.merge())
            db.collection("users").document(user.uid)
                .set(testlo, SetOptions.merge())
        }
    }

    // 상대방의 위도와 경도로 거리를 계산하여 친구목록에 표시될 사람을 골라내는 함수
    fun findFriend(myLatitude: Double, myLongitude: Double) {
        if (user != null) {
            // 파이어베이스에서 다른 사람의 위치 가져오기
            db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id != user.uid) {    //로그인한 본인 데이터를 제외하고
                            val yourLatitude = document.data["latitude"] as Double      // 위도
                            val yourLongitude = document.data["longitude"] as Double    // 경도

                            // 거리 구하기
                            val distance: Double =
                                getDistance(myLatitude, myLongitude, yourLatitude.toDouble(), yourLongitude)

                            // 만약 거리가 300m보다 작다면
                            if (distance <= 300) {
                                // 친구목록 데이터에 해당 사용자 Friends객체로 추가
                                data.add(
                                    Friends(
                                        document.id,
                                        document.data["profileImageUrl"].toString(),
                                        document.data["username"].toString(),
                                        distance.toInt()
                                    )
                                )
                                friendLiveData.value = data // 데이터 업데이트
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->    //실패하였을 경우
                    Log.d("test", "Error getting documents: ", exception)
                }
        }
    }

    // 거리를 구하는 함수
    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dlat = lat1 - lat2
        val dlon = lon1 - lon2

        val distance = sqrt(pow(dlat, 2.0) + pow(dlon, 2.0)) * 100000   // 거리구하기

        return distance // 거리반환
    }
}




