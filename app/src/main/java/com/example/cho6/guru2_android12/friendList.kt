package com.example.cho6.guru2_android12

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cho6.guru2_android12.databinding.ActivityFriendListBinding
import com.example.cho6.guru2_android12.databinding.ItemFriendsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class friendList : AppCompatActivity() {

    private lateinit var binding: ActivityFriendListBinding

    private val friendListViewModel:FriendListViewModel by viewModels()

//    private val data= arrayListOf<Friends>()

    var myDialog: Dialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFriendListBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

//        인텐트에서 위도와 경도 받아오기
        var latitude:Double?=intent.getDoubleExtra("latitude", 1.0)
        var longitude:Double?=intent.getDoubleExtra("longitute", 1.0)

//        위도 경도 withmefriendActivity에서 friendList로 넘어왔는지 확인용 로그
        Log.d("lat", "받아온 위도 : "+latitude)
        Log.d("lat", "받아온 경도 : "+longitude)

        friendListViewModel.addMyLocation(latitude!!, longitude!!)

        Log.d("lat", "완료")




//        data.add(Friends("김채영", "247"))
//        data.add(Friends("정지연", "256"))

        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        //binding.recyclerView.adapter=FriendListAdapter(data)

        myDialog=Dialog(this)
    }

    fun ShowCallOnePop(v:View){
        val textView:TextView
        val butClose:Button
        val butNo:Button
        val butYes:Button
        myDialog?.setContentView(R.layout.call_one)
        textView= myDialog?.findViewById(R.id.nickname_call_one) as TextView
        butClose= myDialog?.findViewById(R.id.close_call_one) as Button
        butYes=myDialog?.findViewById(R.id.yes_call_one) as Button
        butNo=myDialog?.findViewById(R.id.no_call_one) as Button
        butClose.setOnClickListener {
            myDialog!!.dismiss()
        }
        butNo.setOnClickListener {
            myDialog!!.dismiss()
            val failedWindow:Button= myDialog?.findViewById(R.id.no_call_one)!!
            ShowNoPop(failedWindow)
        }
        butYes.setOnClickListener {
            myDialog!!.dismiss()
            val okWindow:Button= myDialog?.findViewById(R.id.yes_call_one)!!
            ShowYesPop(okWindow)
        }
        myDialog!!.show()
    }

    fun ShowCallAllPop(v:View){
        val textView:TextView
        val butClose:Button
        val butNo:Button
        val butYes:Button
        myDialog?.setContentView(R.layout.call_all)
        textView= myDialog?.findViewById(R.id.nickname_call_one) as TextView
        butClose= myDialog?.findViewById(R.id.close_call_one) as Button
        butYes=myDialog?.findViewById(R.id.yes_call_one) as Button
        butNo=myDialog?.findViewById(R.id.no_call_one) as Button
        butClose.setOnClickListener {
            myDialog!!.dismiss()
        }
        butNo.setOnClickListener {
            myDialog!!.dismiss()
            val failedWindow:Button= myDialog?.findViewById(R.id.no_call_one)!!
            ShowNoPop(failedWindow)
        }
        butYes.setOnClickListener {
            myDialog!!.dismiss()
            val okWindow:Button= myDialog?.findViewById(R.id.yes_call_one)!!
            ShowYesPop(okWindow)
        }
        myDialog!!.show()
    }

    fun ShowYesPop(v:View){
        val butClose:Button
        myDialog?.setContentView(R.layout.call_ok)
        butClose= myDialog?.findViewById(R.id.close_call_one) as Button
        butClose.setOnClickListener {
            myDialog!!.dismiss()
        }
        myDialog!!.show()
    }

    fun ShowNoPop(v:View){
        val butClose:Button
        myDialog?.setContentView(R.layout.call_fail)
        butClose= myDialog?.findViewById(R.id.close_call_one) as Button
        butClose.setOnClickListener {
            myDialog!!.dismiss()
        }
        myDialog!!.show()
    }
}

data class Friends(val nickname:String, var distance:String)

class FriendListAdapter(private val myDataset: List<Friends>) :
    RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder>() {

    class FriendListViewHolder(val binding: ItemFriendsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): FriendListAdapter.FriendListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friends, parent, false)
        return FriendListViewHolder(ItemFriendsBinding.bind(view))
    }

    override fun onBindViewHolder(holder: FriendListViewHolder, position: Int) {
        holder.binding.nickname.text=myDataset[position].nickname
        holder.binding.distance.text=myDataset[position].distance
    }

    override fun getItemCount() = myDataset.size
}

class FriendListViewModel:ViewModel(){
    val db = Firebase.firestore

    private val data= arrayListOf<Friends>()

    fun addMyLocation(latitude:Double, longitude:Double){
        val user= FirebaseAuth.getInstance().currentUser
        if(user!=null) {
            db.collection("users").document("tDqIF2oBx1bvUmCgaDwN").update("latitude", latitude)
            db.collection("users").document("tDqIF2oBx1bvUmCgaDwN").update("longitude", longitude)
        }

    }

//    fun whoCanWithme{
//
//    }
//
//    fun calculateDistance

}