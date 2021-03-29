
package com.example.technicaltest.view.users

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.example.technicaltest.R
import com.example.technicaltest.databinding.ActivityMainBinding
import com.example.technicaltest.model.UserItem
import com.example.technicaltest.utils.*
import com.example.technicaltest.view.albums.UserAlbumsListActivity
import com.example.technicaltest.view.users.adapter.UsersListAdapter
import com.example.technicaltest.viewmodel.UsersViewModel
import dev.shreyaspatil.foodium.ui.base.BaseActivity
import okhttp3.internal.toImmutableList
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<UsersViewModel, ActivityMainBinding>(){


    override val mViewModel: UsersViewModel by viewModel()

    private val usersListAdapter = UsersListAdapter(this::onItemClicked)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        mViewBinding.usersRecyclerView.adapter = usersListAdapter

        initUsersList()
        searchAction()
        handleNetworkChanges()
    }

    private fun searchAction() {
        mViewBinding.searchBar.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    usersListAdapter.filter.filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            setOnCloseListener {
                usersListAdapter.resetSearch()
                usersListAdapter.notifyDataSetChanged()
                true
            }
        }


    }
    private fun initUsersList() {
        mViewModel.usersListLiveData.observe(this) { state ->
            when (state) {
                is State.Loading -> showLoading(true)
                is State.Success -> {
                    if (state.data.isNotEmpty()) {
                        usersListAdapter.setUsers(state.data.toMutableList())
                        usersListAdapter.notifyDataSetChanged()
                        showLoading(false)
                    }
                }
                is State.Error -> {
                    showToast(state.message)
                    showLoading(false)
                    // If State isn't `Success` then reload posts.
                    //getUsersList() <- je commente car je trouve ça consommateur d'energie.
                    // SI on arrive pas à charger les données, c'est qu'il y a un soucis, par ex l'utilisateur peux avoir une connexion internet super faible,
                    // ton "handleNetworkChanges", te dis bien qu'il y a une connexion mais elle est tellement faible qu'on ne peut charger les données
                    //il est préférable d'ajouter un bouton "retry" sur l'écran en cas d'erreur et de mapper le click sur ce bouton à "viewmodel.retry()"
                }
            }
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
                    setBackgroundColor(ContextCompat.getColor(this@MainActivity,R.color.colorStatusNotConnected))
                }
            } else {
                if (mViewModel.usersListLiveData.value is State.Error || usersListAdapter.itemCount == 0) {
                    //getUsersList()
                    //cf commentaire ligne 84
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
