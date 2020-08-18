package com.example.cho6.guru2_android12

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cho6.guru2_android12.databinding.ActivityFriendListBinding
import com.example.cho6.guru2_android12.databinding.ItemFriendsBinding
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Math.pow
import kotlin.math.sqrt

class friendList : AppCompatActivity() {

    private lateinit var binding: ActivityFriendListBinding

    private val friendListViewModel: FriendListViewModel by viewModels()

//    private val data= arrayListOf<Friends>()

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

//        friendListViewModel.data.add(Friends("김채영", "247"))
//        friendListViewModel.data.add(Friends("정지연", "256"))



        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = FriendListAdapter(friendListViewModel.data)

        myDialog = Dialog(this)


    }

    fun ShowCallOnePop(v: View) {
        val textView: TextView
        val butClose: Button
        val butNo: Button
        val butYes: Button
        myDialog?.setContentView(R.layout.call_one)
        textView = myDialog?.findViewById(R.id.nickname_call_one) as TextView
        butClose = myDialog?.findViewById(R.id.close_call_one) as Button
        butYes = myDialog?.findViewById(R.id.yes_call_one) as Button
        butNo = myDialog?.findViewById(R.id.no_call_one) as Button
        butClose.setOnClickListener {
            myDialog!!.dismiss()
        }
        butNo.setOnClickListener {
            myDialog!!.dismiss()
            val failedWindow: Button = myDialog?.findViewById(R.id.no_call_one)!!
            ShowNoPop(failedWindow)
        }
        butYes.setOnClickListener {
            myDialog!!.dismiss()
            val okWindow: Button = myDialog?.findViewById(R.id.yes_call_one)!!
            ShowYesPop(okWindow)
        }
        myDialog!!.show()
    }

    fun ShowCallAllPop(v: View) {
        val textView: TextView
        val butClose: Button
        val butNo: Button
        val butYes: Button
        myDialog?.setContentView(R.layout.call_all)
        textView = myDialog?.findViewById(R.id.nickname_call_one) as TextView
        butClose = myDialog?.findViewById(R.id.close_call_one) as Button
        butYes = myDialog?.findViewById(R.id.yes_call_one) as Button
        butNo = myDialog?.findViewById(R.id.no_call_one) as Button
        butClose.setOnClickListener {
            myDialog!!.dismiss()
        }
        butNo.setOnClickListener {
            myDialog!!.dismiss()
            val failedWindow: Button = myDialog?.findViewById(R.id.no_call_one)!!
            ShowNoPop(failedWindow)
        }
        butYes.setOnClickListener {
            myDialog!!.dismiss()
            val okWindow: Button = myDialog?.findViewById(R.id.yes_call_one)!!
            ShowYesPop(okWindow)
        }
        myDialog!!.show()
    }

    fun ShowYesPop(v: View) {
        val butClose: Button
        myDialog?.setContentView(R.layout.call_ok)
        butClose = myDialog?.findViewById(R.id.close_call_one) as Button
        butClose.setOnClickListener {
            myDialog!!.dismiss()
        }
        myDialog!!.show()
    }

    fun ShowNoPop(v: View) {
        val butClose: Button
        myDialog?.setContentView(R.layout.call_fail)
        butClose = myDialog?.findViewById(R.id.close_call_one) as Button
        butClose.setOnClickListener {
            myDialog!!.dismiss()
        }
        myDialog!!.show()
    }
}

data class Friends(
    val nickname: String,
    var distance: Int)

class FriendListAdapter(private val myDataset: List<Friends>) :
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
    }

    override fun getItemCount() = myDataset.size
}

class FriendListViewModel : ViewModel() {
    val db = Firebase.firestore

    val data = arrayListOf<Friends>()

    // 내 위치를 데이터베이스에 저장하기 위한 함수=>성공
    fun addMyLocation(myLatitude: Double, myLongitude: Double) {
        val testla= hashMapOf("latitude" to myLatitude)
        val testlo= hashMapOf("longitude" to myLongitude)

        db.collection("users").document("tDqIF2oBx1bvUmCgaDwN")
            .set(testla, SetOptions.merge())
        db.collection("users").document("tDqIF2oBx1bvUmCgaDwN")
            .set(testlo, SetOptions.merge())
    }

    // 상대방의 위도와 경도로 거리를 계산하여 화면에 표시될 사람을 골라내는 함수
    fun findFriend(myLatitude: Double, myLongitude: Double){
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.id=="fGNgb3sYNOcl0ys5oqTC"){    //로그인한 본인 데이터가 아닌 다른사람들
                        val yourLatitude=document.data["latitude"] as Double
                        val yourLongitude=document.data["longitude"] as Double
                        val distance : Double = getDistance(myLatitude, myLongitude, yourLatitude, yourLongitude)
                        if(distance<=300){
                            Log.d("test", ""+document.data["username"])
                            data.add(Friends(document.data["username"].toString(), distance.toInt()))
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("test", "Error getting documents: ", exception)
            }
    }

    // 리사이클뷰에 추가하는 함수
    fun addRecyclerView(friendlist : Friends){
        data.add(friendlist)
    }

    // 거리를 구하는 함수
    fun getDistance(lat1:Double, lon1:Double, lat2:Double, lon2:Double): Double {
        val dlat=lat1-lat2
        val dlon=lon1-lon2

        val distance=sqrt(pow(dlat,2.0)+ pow(dlon,2.0))*100000

        return distance
    }
}