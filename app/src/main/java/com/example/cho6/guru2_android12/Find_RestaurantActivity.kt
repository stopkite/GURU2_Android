package com.example.cho6.guru2_android12

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class Find_RestaurantActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_restaurant)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


        // 하단부 메뉴 fragment 설정
        val fragmentmenu: BottomMenu = BottomMenu()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragmentmenu)
        fragmentTransaction.commit()


        // 오른쪽 상단 버튼(근처 맛집 찾기) 눌렀을 때
        val nearsearch = findViewById<ImageView>(R.id.near_restaurant)
        nearsearch.setOnClickListener {
            Log.d("please", "plz")
            popupAgree()    // 동의서 팝업창 뜨는 함수
        }


    }

    // 지도 관련 그대로 긁어온 것
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val SEOUL = LatLng(37.56, 126.97)
        val markerOptions = MarkerOptions()
        markerOptions.position(SEOUL)
        markerOptions.title("서울")
        markerOptions.snippet("한국의 수도")
        mMap!!.addMarker(markerOptions)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(SEOUL))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
    }

    // 동의서 팝업창 띄우는 함수
    private fun popupAgree() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup, null)

        val alertDialog = AlertDialog.Builder(this).setTitle("위치정보 이용 동의")
            .setPositiveButton("동의합니다"){dialog,which ->
                screen_change() // 화면전환 + 토스트
            }
            .setNeutralButton("취소",null)
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }

    // '동의합니다' 눌렀을 때 화면전환 + 토스트 띄우는 함수
    fun screen_change(){

        Toast.makeText(applicationContext, "동의하셨습니다", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, nearsearch::class.java)
        startActivity(intent)
    }



}