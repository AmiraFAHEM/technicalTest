
package com.example.technicaltest.view.users.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.technicaltest.databinding.ItemAlbumBinding
import com.example.technicaltest.model.AlbumsItem
import dev.shreyaspatil.foodium.ui.main.viewholder.AlbumViewHolder

class AlbumsListAdapter(
    private val onItemClicked: (AlbumsItem, ImageView) -> Unit
) : ListAdapter<AlbumsItem, AlbumViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AlbumViewHolder(
        ItemAlbumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClicked)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AlbumsItem>() {
            override fun areItemsTheSame(oldItem: AlbumsItem, newItem: AlbumsItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AlbumsItem, newItem: AlbumsItem): Boolean =
                oldItem == newItem
        }
    }
}
