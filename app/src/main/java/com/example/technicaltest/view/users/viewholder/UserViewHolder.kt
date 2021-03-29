/*
 * MIT License
 *
 * Copyright (c) 2020 Shreyas Patil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.technicaltest.view.users.viewholder

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.technicaltest.ApplicationCore
import com.example.technicaltest.R
import com.example.technicaltest.databinding.ItemUserBinding
import com.example.technicaltest.model.UserItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun bind(user: UserItem, onItemClicked: (UserItem, ImageView) -> Unit) {
        binding.nameTv.text = ApplicationCore.context.getString(R.string.name,user.name)
        binding.userNameTv.text = ApplicationCore.context.getString(R.string.username,user.username)
        binding.webSiteTv.text = ApplicationCore.context.getString(R.string.website,user.website)
        binding.emailTv.text = ApplicationCore.context.getString(R.string.email,user.email)
        binding.phoneTv.text = ApplicationCore.context.getString(R.string.phone,user.phone)

        binding.root.setOnClickListener {
            onItemClicked(user, binding.imageView)
        }
    }
}
