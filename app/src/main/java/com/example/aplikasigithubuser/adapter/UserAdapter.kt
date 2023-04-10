package com.example.aplikasigithubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.databinding.UserItemBinding
import com.example.aplikasigithubuser.data.model.Items

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private val listUser = ArrayList<Items>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(private val _binding: UserItemBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        fun bindItem(user: Items) {
            _binding.tvItemName.text = user.login

            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .skipMemoryCache(true)
                .into(_binding.imgItemPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        holder.bindItem(user)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(
                listUser[position]
            )
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    fun interface OnItemClickCallback {
        fun onItemClicked(data: Items)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(data: ArrayList<Items>) {
        val diffCallback = DiffUtilCallback(listUser, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listUser.clear()
        listUser.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    class DiffUtilCallback(private val oldList: List<Items>, private val newList: List<Items>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.hashCode() == newItem.hashCode()
        }

        @Override
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}