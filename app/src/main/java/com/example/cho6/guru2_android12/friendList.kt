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

class friendList : AppCompatActivity() {

    private lateinit var binding: ActivityFriendListBinding

    private val friendListViewModel: FriendListViewModel by viewModels()

    var myDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        인텐트에서 위도와 경도 받아오기
        var latitude: Double? = intent.getDoubleExtra("latitude", 1.0)
        var longitude: Double? = intent.getDoubleExtra("longitute", 1.0)

        binding = ActivityFriendListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        friendListViewModel.addMyLocation(latitude!!, longitude!!)
        friendListViewModel.findFriend(latitude!!, longitude!!)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = FriendListAdapter(emptyList())


        //관찰 UI 업데이트
        friendListViewModel.friendLiveData.observe(this, Observer {
            (binding.recyclerView.adapter as FriendListAdapter).setData(it as ArrayList<Friends>)
        })

        callAllButton.setOnClickListener {
            val callallpopup= Intent(this, CallAll::class.java)
            callallpopup.apply {
                this.putExtra("size", friendListViewModel.getData().size)
            }
            startActivity(callallpopup)
        }
    }
}

// Friends 객체
data class Friends(
    val userid: String,
    val pic: String,
    val nickname: String,
    var distance: Int
)

// 리사이클러 뷰 어댑터
class FriendListAdapter(private var myDataset: List<Friends>) :
    RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder>() {

    class FriendListViewHolder(val binding: ItemFriendsBinding) :
        RecyclerView.ViewHolder(binding.root)

    var goChatData: Friends?=null

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

        // 버튼을 누르면 현재 누른 아이템이 몇번째 아이템인지 반환
        // goChatData에 현재 위치의 Friends 데이터 저장
        holder.binding.withmeone.setOnClickListener {
            val startPopup: ImageButton = holder.binding.withmeone
            val context: Context = startPopup.getContext()
            val pos: Int = holder.getAdapterPosition()

            val callonepopup= Intent(context, CallOne::class.java)
            val bundle = Bundle()
            bundle.putString("usernickname", myDataset[pos].nickname)
            bundle.putString("useruid", myDataset[pos].userid)
            callonepopup.putExtras(bundle)
            context.startActivity(callonepopup)
        }


    }

    override fun getItemCount() = myDataset.size

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

    fun getData():ArrayList<Friends>{
        friendLiveData.value = data
        return data
    }


    // 내 위치를 데이터베이스에 저장하기 위한 함수=>성공
    fun addMyLocation(myLatitude: Double, myLongitude: Double) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val testla = hashMapOf("latitude" to myLatitude)
            val testlo = hashMapOf("longitude" to myLongitude)

            db.collection("users").document(user.uid)
                .set(testla, SetOptions.merge())
            db.collection("users").document(user.uid)
                .set(testlo, SetOptions.merge())
        }
    }

    // 상대방의 위도와 경도로 거리를 계산하여 화면에 표시될 사람을 골라내는 함수
    fun findFriend(myLatitude: Double, myLongitude: Double) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {

            db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id != user.uid) {    //로그인한 본인 데이터가 아닌 다른사람들
                            val yourLatitude = document.data["latitude"] as Double 
                            val yourLongitude = document.data["longitude"] as Double
                            val distance: Double =
                                getDistance(myLatitude, myLongitude, yourLatitude.toDouble(), yourLongitude)
                            if (distance <= 300) {
                                Log.d("test", "" + document.data["username"])
                                data.add(
                                    Friends(
                                        document.id,
                                        document.data["profileImageUrl"].toString(),
                                        document.data["username"].toString(),
                                        distance.toInt()
                                    )
                                )
                                friendLiveData.value = data
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("test", "Error getting documents: ", exception)
                }


        }
    }

    // 거리를 구하는 함수 => 성공
    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dlat = lat1 - lat2
        val dlon = lon1 - lon2

        val distance = sqrt(pow(dlat, 2.0) + pow(dlon, 2.0)) * 100000

        return distance
    }

    //데이터 베이스에 true false 전환해서 팝업 요청하기(채영 도전!!!!!!!)
    fun goSign(userUid:String){

    }

}




