package com.example.technicaltest.view.photos.viewholder


import androidx.recyclerview.widget.RecyclerView

import com.example.technicaltest.databinding.ItemPhotoBinding

import com.example.technicaltest.model.PhotosItem
import com.squareup.picasso.Picasso


class PhotoViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: PhotosItem) {
        binding.titleTv.text = photo.title
        Picasso.get().load(photo.url).fit().centerCrop().into(binding.imageView)
    }
}
