package com.sirius.miracletools.permissionlist

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sirius.miracletools.R
import com.sirius.miracletools.databinding.PermissionListFragmentBinding

class PermissionsListFragment : Fragment() {

    companion object {
        fun newInstance() = PermissionsListFragment()
    }

    private lateinit var viewModel: PermissionListViewModel
    private lateinit var binding: PermissionListFragmentBinding
    private var permissionListAdapter =
        PermissionListAdapter(HashMap<String, MutableList<ApplicationInfo>>())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PermissionListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PermissionListViewModel::class.java)

        binding.permissionListView.apply {
            adapter = permissionListAdapter
            binding.refreshLayout.setOnRefreshListener {
                binding.permissionListView.visibility = View.GONE
                binding.listError.visibility = View.GONE
                binding.loadingView.visibility = View.VISIBLE
                viewModel.refresh()
                binding.refreshLayout.isRefreshing = false
            }
        }
        observerModel()
        viewModel.refresh()
    }

    private fun observerModel() {
        viewModel.permissionsList.observe(viewLifecycleOwner, Observer {
            it.let {
                binding.permissionListView.visibility = View.VISIBLE
                permissionListAdapter.updateData(it)
            }
        })
        viewModel.isError.observe(viewLifecycleOwner, Observer {
            it.let {
                if (it) {
                    binding.listError.visibility = View.VISIBLE
                    binding.loadingView.visibility = View.GONE
                    binding.permissionListView.visibility = View.GONE
                } else {
                    binding.listError.visibility = View.GONE
                }
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            it.let {
                if (it) {
                    binding.loadingView.visibility = View.VISIBLE
                    binding.listError.visibility = View.GONE
                    binding.permissionListView.visibility = View.GONE
                } else {
                    binding.loadingView.visibility = View.GONE
                }
            }
        })
    }

}
