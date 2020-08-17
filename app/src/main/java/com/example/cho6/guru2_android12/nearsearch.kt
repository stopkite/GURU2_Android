package com.example.cho6.guru2_android12

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class nearsearch : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.near_search)


        val list_spread = findViewById<ImageView>(R.id.list_spread)
        list_spread.setOnClickListener {
            val intent = Intent(this, restaurant_list::class.java)
            startActivity(intent)
        }



        // 하단부 메뉴 fragment 설정
        val fragmentmenu: BottomMenu = BottomMenu()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragmentmenu)
        fragmentTransaction.commit()

        // 하단부 식당정보
        val fragmentlocation: locationInformation = locationInformation()
        val fragmentManager2: FragmentManager = supportFragmentManager
        val fragmentTransaction2 = fragmentManager2.beginTransaction()
        fragmentTransaction2.replace(R.id.locationInformation, fragmentlocation)
        fragmentTransaction2.commit()


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val SCHOOL = LatLng(37.62, 127.09)
        val markerOptions = MarkerOptions()
        markerOptions.position(SCHOOL)
        markerOptions.title("서울여자대학교")
        markerOptions.snippet("서울특별시 노원구 공릉2동 화랑로 621")
        mMap!!.addMarker(markerOptions)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.62, 127.09)))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(13f))
    }
}
