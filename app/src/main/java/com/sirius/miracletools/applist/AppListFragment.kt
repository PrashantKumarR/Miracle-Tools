package com.sirius.miracletools.applist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sirius.miracletools.databinding.AppListFragmentBinding

class AppListFragment : Fragment() {

    companion object {
        fun newInstance() = AppListFragment()
    }

    private lateinit var viewModel: AppListViewModel
    private var appListAdapter =
        AppInfoAdapter(ArrayList())
    private lateinit var binding: AppListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AppListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AppListViewModel::class.java)
        viewModel.loadAppList()

        binding.appListView.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = appListAdapter
            binding.refreshLayout.setOnRefreshListener {
                binding.appListView.visibility = View.GONE
                binding.listError.visibility = View.GONE
                binding.loadingView.visibility = View.VISIBLE
                viewModel.loadAppList()
                binding.refreshLayout.isRefreshing = false
            }
        }
        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.appList.observe(viewLifecycleOwner, Observer { apps ->
            apps?.let {
                binding.appListView.visibility = View.VISIBLE
                appListAdapter.updateData(it)
            }
        })

        viewModel.isLoadError.observe(viewLifecycleOwner, Observer {
            it.let {
                binding.listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            it.let {
                if (it) {
                    binding.loadingView.visibility = View.VISIBLE
                    binding.appListView.visibility = View.GONE
                    binding.listError.visibility = View.GONE
                } else {
                    binding.loadingView.visibility = View.GONE
                }
            }
        })
    }

}
