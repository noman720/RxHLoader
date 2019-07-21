package com.github.noman720.rxhloader.sample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.noman720.rxhloader.R
import com.github.noman720.rxhloader.databinding.UserItemBinding
import com.github.noman720.rxhloader.sample.model.User
import com.github.noman720.rxhloader.sample.model.UserData

/**
 * Created by Abu Noman on 7/21/19.
 */
class UserAdapter(
    private val onClickListener: OnClickListener
) : ListAdapter<UserData, UserAdapter.UserViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = DataBindingUtil.inflate<UserItemBinding>(LayoutInflater.from(parent.context), R.layout.user_item, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val item = getItem(position)
            holder.itemView.setOnClickListener {
                onClickListener.onClick(item)
            }
            holder.bind(item)
        }
    }

    inner class UserViewHolder(
        private val itemBinding: UserItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: UserData) {
            itemBinding.userData = item
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            itemBinding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<UserData>() {
        override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean =
            oldItem.user.username == newItem.user.username && oldItem.id == newItem.id

        override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean =
            oldItem == newItem
    }

    class OnClickListener(val clickListener: (item: UserData) -> Unit) {
        fun onClick(item: UserData) = clickListener(item)
    }
}
