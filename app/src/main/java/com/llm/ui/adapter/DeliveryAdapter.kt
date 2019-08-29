package com.llm.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llm.R
import com.llm.data.models.DeliveryItemDataModel
import com.llm.data.network.NetworkState
import javax.inject.Inject

class DeliveryAdapter @Inject constructor() :
    PagedListAdapter<DeliveryItemDataModel, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    var itemSelectListener: OnItemSelectListener? = null
    private lateinit var retryFunction: () -> Unit
    private var currNetworkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            R.layout.delivery_list_item -> DeliveryItemViewHolder.create(parent, itemSelectListener)

            R.layout.network_state_item -> NetworkStateViewHolder.create(parent, retryFunction)

            else -> throw IllegalArgumentException("No such view type handled")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.delivery_list_item -> (holder as DeliveryItemViewHolder).bindTo(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateViewHolder).bindTo(
                currNetworkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.delivery_list_item
        }
    }


    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0

    }

    fun setRetry(retry: () -> Unit) {
        retryFunction = retry
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.currNetworkState
        val hadExtraRow = hasExtraRow()
        this.currNetworkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow(): Boolean {
        return currNetworkState!=null && currNetworkState != NetworkState.LOADED
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<DeliveryItemDataModel>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldDelivery: DeliveryItemDataModel,
                newDelivery: DeliveryItemDataModel
            ) = oldDelivery.id == newDelivery.id

            override fun areContentsTheSame(
                oldDelivery: DeliveryItemDataModel,
                newDelivery: DeliveryItemDataModel
            ) = oldDelivery == newDelivery
        }
    }

    interface OnItemSelectListener {
        fun onSelect(model: DeliveryItemDataModel)
    }

}