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

package dev.shreyaspatil.foodium.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.example.technicaltest.R
import com.example.technicaltest.databinding.ActivityAlbumPhotosBinding
import com.example.technicaltest.databinding.ActivityAlbumsBinding
import com.example.technicaltest.databinding.ActivityMainBinding
import com.example.technicaltest.model.AlbumsItem
import com.example.technicaltest.utils.*
import com.example.technicaltest.view.photos.adapter.PhotosListAdapter
import com.example.technicaltest.view.users.adapter.AlbumsListAdapter
import dev.shreyaspatil.foodium.ui.base.BaseActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.example.technicaltest.viewmodel.AlbumsViewModel
import com.example.technicaltest.viewmodel.PhotosViewModel
import okhttp3.internal.toImmutableList
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class AlbumPhotosListActivity : BaseActivity<PhotosViewModel, ActivityAlbumPhotosBinding>() {

    override val mViewModel: PhotosViewModel by viewModel()

    private val photosListAdapter = PhotosListAdapter()
    var albumId : Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        mViewBinding.usersRecyclerView.adapter = photosListAdapter
        albumId = intent.extras?.getInt(ALBUM_ID)
            ?: throw IllegalArgumentException("`albumId` must be non-null")

        initAlbumPhotos()
        handleNetworkChanges()
    }

    private fun initAlbumPhotos() {
        mViewModel.albumPhotosListLiveData.observe(this) { state ->
            when (state) {
                is State.Loading -> showLoading(true)
                is State.Success -> {
                    if (!state.data.isEmpty()) {
                        photosListAdapter.submitList(state.data.toImmutableList())
                        showLoading(false)
                    }
                }
                is State.Error -> {
                    showToast(state.message)
                    showLoading(false)
                }
            }
        }

        // If State isn't `Success` then reload posts.
        if (mViewModel.albumPhotosListLiveData.value !is State.Success) {
            getPhotosByAlbumId()
        }
    }

    private fun getPhotosByAlbumId() {
        albumId?.let { albumId ->
            mViewModel.getPhotosByAlbumId(albumId)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            mViewBinding.progressBar.visibility = View.VISIBLE
        } else {
            mViewBinding.progressBar.visibility = View.GONE

        }
    }

    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this) { isConnected ->
            if (!isConnected) {
                mViewBinding.textViewNetworkStatus.text =
                    getString(R.string.text_no_connectivity)
                mViewBinding.networkStatusLayout.apply {
                    show()
                    setBackgroundColor(ContextCompat.getColor(this@AlbumPhotosListActivity,R.color.colorStatusNotConnected))
                }
            } else {
                if (mViewModel.albumPhotosListLiveData.value is State.Error || photosListAdapter.itemCount == 0) {
                    getPhotosByAlbumId()
                }
                mViewBinding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                mViewBinding.networkStatusLayout.apply {
                    setBackgroundColor(ContextCompat.getColor(this@AlbumPhotosListActivity,R.color.colorStatusConnected))

                    animate()
                        .alpha(1f)
                        .setStartDelay(ANIMATION_DURATION)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        }
    }

    override fun getViewBinding(): ActivityAlbumPhotosBinding = ActivityAlbumPhotosBinding.inflate(layoutInflater)


    companion object {
        const val ANIMATION_DURATION = 1000.toLong()
        private const val ALBUM_ID = "albumId"
        fun getStartIntent(
            context: Context,
            userId: Int
        ) = Intent(context, AlbumPhotosListActivity::class.java).apply { putExtra(ALBUM_ID, userId) }

    }
}
