package com.llm.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.llm.R
import com.llm.data.models.DeliveryItemDataModel

class DeliveryItemViewHolder(itemView: View, private val itemSelectListener: DeliveryAdapter.OnItemSelectListener?) :
    RecyclerView.ViewHolder(itemView) {
    private var tvDesc: TextView = itemView.findViewById(R.id.tv_desc)
    private var imageView: SimpleDraweeView = itemView.findViewById(R.id.iv) as SimpleDraweeView


    fun bindTo(item: DeliveryItemDataModel?) {
        itemView.setOnClickListener {
            itemSelectListener!!.onSelect(item!!)
        }

        tvDesc.text = item?.description
        imageView.setImageURI(Uri.parse(item?.imageUrl ?: ""))
    }

    companion object {
        fun create(
            parent: ViewGroup,
            itemSelectListener: DeliveryAdapter.OnItemSelectListener?
        ): DeliveryItemViewHolder {
            return DeliveryItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.delivery_list_item, parent, false),
                itemSelectListener
            )
        }
    }


}