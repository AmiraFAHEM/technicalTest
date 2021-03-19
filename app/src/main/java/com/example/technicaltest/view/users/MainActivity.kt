
package com.example.technicaltest.view.users

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.example.technicaltest.R
import com.example.technicaltest.databinding.ActivityMainBinding
import com.example.technicaltest.model.UserItem
import com.example.technicaltest.utils.*
import dev.shreyaspatil.foodium.ui.base.BaseActivity
import com.example.technicaltest.view.users.adapter.UsersListAdapter
import com.example.technicaltest.viewmodel.UsersViewModel
import dev.shreyaspatil.foodium.ui.main.UserAlbumsListActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.internal.toImmutableList
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class MainActivity : BaseActivity<UsersViewModel, ActivityMainBinding>() , CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var job: Job

    override val mViewModel: UsersViewModel by viewModel()

    private val usersListAdapter = UsersListAdapter(this::onItemClicked)

    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        job = Job()
        mViewBinding.usersRecyclerView.adapter = usersListAdapter

        initUsersList()
        searchAction()
        handleNetworkChanges()
    }

    @FlowPreview
    private fun searchAction() {
        launch{
            searchBar.getQueryTextChangeStateFlow()
                .debounce(300)
                .filter { query ->
                    if (query.isEmpty()) {
                        withContext(Dispatchers.Main) {
                            getUsersList()
                        }
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    mViewModel.getUsersSearchResult(query)
                }
                .flowOn(Dispatchers.Default)
                .collect { result ->
                    withContext(Dispatchers.Main) {
                        if (result is State.Success) {
                            usersListAdapter.submitList(result.data)
                            usersListAdapter.notifyDataSetChanged()
                        }
                    }
                }
        }
    }
    private fun initUsersList() {
        mViewModel.usersListLiveData.observe(this) { state ->
            when (state) {
                is State.Loading -> showLoading(true)
                is State.Success -> {
                    if (!state.data.isEmpty()) {
                        usersListAdapter.submitList(state.data.toImmutableList())
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
        if (mViewModel.usersListLiveData.value !is State.Success) {
            getUsersList()
        }
    }

    private fun getUsersList() {
        mViewModel.getUsersList()
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
                    setBackgroundColor(ContextCompat.getColor(this@MainActivity,R.color.colorStatusNotConnected))
                }
            } else {
                if (mViewModel.usersListLiveData.value is State.Error || usersListAdapter.itemCount == 0) {
                    getUsersList()
                }
                mViewBinding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                mViewBinding.networkStatusLayout.apply {
                    setBackgroundColor(ContextCompat.getColor(this@MainActivity,R.color.colorStatusConnected))

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

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private fun onItemClicked(userItem: UserItem, imageView: ImageView) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            imageView,
            imageView.transitionName
        )
        val userId = userItem.id
        val intent = UserAlbumsListActivity.getStartIntent(this, userId)
        startActivity(intent, options.toBundle())
    }

    companion object {
        const val ANIMATION_DURATION = 1000.toLong()
    }
}
