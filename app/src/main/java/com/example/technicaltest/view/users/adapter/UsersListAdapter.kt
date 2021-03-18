
package com.example.technicaltest.view.users.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.technicaltest.databinding.ItemUserBinding
import com.example.technicaltest.model.UserItem
import dev.shreyaspatil.foodium.ui.main.viewholder.UserViewHolder

class UsersListAdapter(
    private val onItemClicked: (UserItem, ImageView) -> Unit
) : ListAdapter<UserItem, UserViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClicked)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserItem>() {
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean =
                oldItem == newItem
        }
    }
}
