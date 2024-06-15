package com.example.bookmate.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.R
import com.example.bookmate.data.response.BookItem
import com.example.bookmate.databinding.FragmentHomeBinding
import com.example.bookmate.utils.ViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = obtainViewModel(this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        setupAction()
    }

    private fun setupObserver() {
        viewModel.getData()

        viewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        viewModel.listBuku.observe(requireActivity()) {
            if (it.isEmpty()) {
                binding.tvNoData.visibility = View.VISIBLE
            } else {
                binding.tvNoData.visibility = View.GONE
                showGenreData(it)
            }
        }

        viewModel.isError.observe(requireActivity()) {
            if (it) {
                showToast(viewModel.getErrorMessage())
            }
        }
    }

    private fun setupAction() {

    }

    private fun showGenreData(listBuku: List<BookItem>) {
        val adapter = BookAdapter()
        adapter.submitList(listBuku)
        adapter.setOnItemClickCallback(object : BookAdapter.OnItemClickCallback {
            override fun onItemClicked(data: BookItem) {
                showToast(data.judul)
            }
        })
        binding.rvBook.adapter = adapter
    }

    private fun obtainViewModel(fragment: Fragment): HomeViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireContext())
        return ViewModelProvider(fragment, factory)[HomeViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }
}