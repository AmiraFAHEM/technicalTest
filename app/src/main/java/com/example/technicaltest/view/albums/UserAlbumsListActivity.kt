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
import com.example.technicaltest.R
import com.example.technicaltest.databinding.ActivityAlbumsBinding
import com.example.technicaltest.databinding.ActivityMainBinding
import com.example.technicaltest.model.AlbumsItem
import com.example.technicaltest.utils.*
import com.example.technicaltest.view.users.adapter.AlbumsListAdapter
import dev.shreyaspatil.foodium.ui.base.BaseActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.example.technicaltest.viewmodel.AlbumsViewModel
import okhttp3.internal.toImmutableList
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class UserAlbumsListActivity : BaseActivity<AlbumsViewModel, ActivityAlbumsBinding>() {

    override val mViewModel: AlbumsViewModel by viewModel()

    private val albumsListAdapter = AlbumsListAdapter(this::onItemClicked)
    var userId : Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        mViewBinding.usersRecyclerView.adapter = albumsListAdapter
        userId = intent.extras?.getInt(USER_ID)
            ?: throw IllegalArgumentException("`userId` must be non-null")

        initAlbums()
        handleNetworkChanges()
    }

    private fun initAlbums() {
        mViewModel.userAlbumsListLiveData.observe(this) { state ->
            when (state) {
                is State.Loading -> showLoading(true)
                is State.Success -> {
                    if (!state.data.isEmpty()) {
                        albumsListAdapter.submitList(state.data.toImmutableList())
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
        if (mViewModel.userAlbumsListLiveData.value !is State.Success) {
            getUserAlbums()
        }
    }

    private fun getUserAlbums() {
        userId?.let { userId ->
            mViewModel.getUserAlbums(userId)
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
                    setBackgroundColor(ContextCompat.getColor(this@UserAlbumsListActivity,R.color.colorStatusNotConnected))
                }
            } else {
                if (mViewModel.userAlbumsListLiveData.value is State.Error || albumsListAdapter.itemCount == 0) {
                    getUserAlbums()
                }
                mViewBinding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                mViewBinding.networkStatusLayout.apply {
                    setBackgroundColor(ContextCompat.getColor(this@UserAlbumsListActivity,R.color.colorStatusConnected))

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

    override fun getViewBinding(): ActivityAlbumsBinding = ActivityAlbumsBinding.inflate(layoutInflater)

    private fun onItemClicked(albumsItem: AlbumsItem, imageView: ImageView) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            imageView,
            imageView.transitionName
        )
        val albumId = albumsItem.id
        val intent = AlbumPhotosListActivity.getStartIntent(this, albumId)
        startActivity(intent, options.toBundle())
    }

    companion object {
        const val ANIMATION_DURATION = 1000.toLong()
        private const val USER_ID = "userId"
        fun getStartIntent(
            context: Context,
            userId: Int
        ) = Intent(context, UserAlbumsListActivity::class.java).apply { putExtra(USER_ID, userId) }

    }
}
