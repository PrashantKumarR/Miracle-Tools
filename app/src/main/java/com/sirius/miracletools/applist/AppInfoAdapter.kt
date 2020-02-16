package com.sirius.miracletools.applist

import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sirius.miracletools.databinding.ItemPkgInfoBinding

class AppInfoAdapter(private val appsList: ArrayList<ResolveInfo>) :
    RecyclerView.Adapter<AppInfoAdapter.AppInfoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoHolder {
        val binding = ItemPkgInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppInfoHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return appsList.size
    }

    override fun onBindViewHolder(holder: AppInfoHolder, position: Int) {
        holder.binding.apply {
            info = appsList[position]
            context = holder.itemView.context
            iconUrl.setImageDrawable(
                holder.itemView.context.packageManager.getApplicationIcon(
                    appsList[position].activityInfo.packageName
                )
            )
        }

    }

    fun updateData(newAppList: List<ResolveInfo>) {
        appsList.clear()
        appsList.addAll(newAppList)
        notifyDataSetChanged()

    }

    class AppInfoHolder(val binding: ItemPkgInfoBinding) : RecyclerView.ViewHolder(binding.root)

}