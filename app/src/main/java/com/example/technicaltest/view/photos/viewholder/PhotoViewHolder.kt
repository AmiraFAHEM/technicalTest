package com.example.technicaltest.view.photos.viewholder


import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.technicaltest.R

import com.example.technicaltest.databinding.ItemPhotoBinding

import com.example.technicaltest.model.PhotosItem


class PhotoViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: PhotosItem) {
        binding.titleTv.text = photo.title
        binding.imageView.load(photo.url) {
            placeholder(R.drawable.ic_photo)
            error(R.drawable.ic_broken_image)
        }
    }
}
