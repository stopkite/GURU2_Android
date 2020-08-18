// ChatListActivity에서 name을 받아주기 위해 만든 kt
package com.example.cho6.guru2_android12.Adapter

import com.example.cho6.guru2_android12.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_list.view.*

//CharListActivity에서 사용될 UserItem 클래스
class UserItem (val name:String, val uid:String, val profileImagUrl: String): Item<GroupieViewHolder>(){

    override fun getLayout(): Int{
        return R.layout.chat_list
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.name.text = name

        Picasso.get().load(profileImagUrl).into(viewHolder.itemView.imageview_new_message)
    }
}