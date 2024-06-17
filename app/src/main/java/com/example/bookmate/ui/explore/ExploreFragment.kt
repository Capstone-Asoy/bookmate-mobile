package com.example.bookmate.ui.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.databinding.FragmentExploreBinding
import com.example.bookmate.utils.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var viewModel: ExploreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(layoutInflater)
        val root: View = binding.root

        viewModel = obtainViewModel(this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getGenres()

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        viewModel.isErrorGetData.observe(viewLifecycleOwner) {
            if (it) {
                showToast(viewModel.getErrorMessage())
            }
        }
        viewModel.listGenre.observe(viewLifecycleOwner) {
            val listTabGenre = it.map { genre -> TabGenreData(genre) }
            val adapter = SectionsPagerAdapter(this, listTabGenre)
            binding.viewPager.adapter = adapter

            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                tab.text = listTabGenre[position].genre
            }.attach()
        }
    }

    private fun obtainViewModel(fragment: Fragment): ExploreViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireContext())
        return ViewModelProvider(fragment, factory)[ExploreViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }
}