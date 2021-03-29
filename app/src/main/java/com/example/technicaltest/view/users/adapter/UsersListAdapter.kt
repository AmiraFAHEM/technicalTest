
package com.example.technicaltest.view.users.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.technicaltest.databinding.ItemUserBinding
import com.example.technicaltest.model.UserItem
import com.example.technicaltest.view.users.viewholder.UserViewHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UsersListAdapter(
    private val onItemClicked: (UserItem, ImageView) -> Unit
) : RecyclerView.Adapter<UserViewHolder>(), Filterable {

    private var unfilteredList = emptyList<UserItem>()
    private var userList = emptyList<UserItem>()

    private val userFilter = UserFilter()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    fun setUsers(users : List<UserItem>){
        unfilteredList = users
        userList = users
    }

    fun resetSearch(){
        setUsers(unfilteredList)
    }



    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(userList[position], onItemClicked)


    override fun getFilter(): Filter {
        return userFilter
    }

    inner class UserFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val result = FilterResults()
            if(constraint != null && constraint.isNotEmpty()){
                val filteredList = mutableListOf<UserItem>()
                for(user in unfilteredList){
                    if(user.name.toUpperCase().contains(constraint.toString().toUpperCase())
                        || user.username.toUpperCase().contains(constraint.toString().toUpperCase())
                    ){
                        filteredList.add(user)
                    }
                }
                result.count = filteredList.size
                result.values = filteredList
            } else {
                result.count = unfilteredList.size
                result.values = unfilteredList
            }
            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if(results != null){
                (results.values as? ArrayList<UserItem>)?.let {
                    userList = it
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
