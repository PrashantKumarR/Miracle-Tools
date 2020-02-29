package com.sirius.miracletools.permissionlist

import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sirius.miracletools.databinding.PermissionItemsBinding

class PermissionListAdapter(private val permissionList: MutableMap<String, MutableList<ApplicationInfo>>) :
    RecyclerView.Adapter<PermissionListAdapter.PermissionViewHolder>() {

    private val _tag = PermissionListAdapter::class.java.simpleName
    private lateinit var _keys: List<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        val binding =
            PermissionItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PermissionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return permissionList.size
    }

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        holder.binding.apply {
            permission = _keys[position]
            context = holder.itemView.context
        }
    }

    fun updateData(updatedPermissionList: MutableMap<String, MutableList<ApplicationInfo>>) {
        permissionList.clear()
        permissionList.putAll(updatedPermissionList)
        _keys = permissionList.keys.toList()
        notifyDataSetChanged()
    }

    class PermissionViewHolder(val binding: PermissionItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

}