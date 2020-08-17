package com.example.cho6.guru2_android12.Adapter

import com.example.cho6.guru2_android12.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_right_me.view.*


class ChatRightMe (val msg: String): Item<GroupieViewHolder>(){
    override fun getLayout(): Int{
        return R.layout.chat_right_me
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.right_msg.text = msg
    }
}