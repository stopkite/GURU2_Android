package com.example.cho6.guru2_android12

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class MainListAdapter(val context: Context, val rList: ArrayList<R_list>): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View = LayoutInflater.from(context).inflate(R.layout.restaurant_list,null)


        val foodphoto = view.findViewById<ImageView>(R.id.photo)
        val name = view.findViewById<TextView>(R.id.restaurant_name)
        val sellfood = view.findViewById<TextView>(R.id.food)
        val loca = view.findViewById<TextView>(R.id.location)
        val g = view.findViewById<TextView>(R.id.good)
        val b = view.findViewById<TextView>(R.id.bad)

        val RList = rList[position]
        val resourceID = context.resources.getIdentifier(RList.photo, "drawable", context.packageName)
        foodphoto.setImageResource(resourceID)
        name.text = RList.restaurant_name
        sellfood.text = RList.food
        loca.text = RList.location
        g.text = RList.good.toString()
        b.text = RList.bad.toString()

        return view

    }

    override fun getItem(p0: Int): Any {
        return rList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return rList.size
    }


}