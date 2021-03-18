
package com.example.technicaltest.view.photos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.technicaltest.databinding.ItemPhotoBinding
import com.example.technicaltest.model.PhotosItem
import com.example.technicaltest.view.photos.viewholder.PhotoViewHolder

class PhotosListAdapter : ListAdapter<PhotosItem, PhotoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhotoViewHolder(
        ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhotosItem>() {
            override fun areItemsTheSame(oldItem: PhotosItem, newItem: PhotosItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PhotosItem, newItem: PhotosItem): Boolean =
                oldItem == newItem
        }
    }
}
