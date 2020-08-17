package com.example.cho6.guru2_android12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.cho6.guru2_android12.restaurant_list
import kotlinx.android.synthetic.main.activity_restaurant_list.*

class restaurant_list : AppCompatActivity() {

    var restaurant = arrayListOf<R_list>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        val rAdapter = MainListAdapter(this, restaurant)
        mainListView.adapter = rAdapter

        var restaurant = arrayListOf<R_list>(
            R_list(
                "#000000", "가게이름", "분식", "화랑대", 5, 2
            ),
            R_list("24시간 국밥", "콩나물국밥", "가양", "#000000", 5, 2),
            R_list("here we go", "술", "발산", "#ff2500", 8, 15),
            R_list("그거알아?", "디저트카페", "당산", "#ffffff", 88, 23),
            R_list("몰라", "피자", "합정", "#226655", 0, 2),
            R_list("IDK", "와플", "화랑대", "#526359", 8, 0 - 1),
            R_list("quit", "쉿", "너의 마음속", "#568978", 1000, 0)
        )

        // 하단부 메뉴 fragment 설정
        val fragmentmenu: BottomMenu = BottomMenu()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragmentmenu)
        fragmentTransaction.commit()

    }


}