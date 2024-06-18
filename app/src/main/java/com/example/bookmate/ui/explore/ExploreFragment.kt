package com.example.bookmate.ui.explore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.components.SearchField
import com.example.bookmate.data.response.BookItem
import com.example.bookmate.databinding.FragmentExploreBinding
import com.example.bookmate.ui.bookdetail.BookDetailActivity
import com.example.bookmate.ui.home.BookAdapter
import com.example.bookmate.utils.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var viewModel: ExploreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = obtainViewModel(this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        search()
    }

    private fun setupAction() {
        binding.edSearch.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH || keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.edSearch.windowToken, 0)

                search()

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.edSearch.setOnItemClickCallback(object : SearchField.OnItemClickCallback {
            override fun onItemClicked() {
                viewModel.getGenres()
            }
        })
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
        viewModel.isErrorSearch.observe(viewLifecycleOwner) {
            if (it) {
                showToast(viewModel.getErrorMessage())
                viewModel.getGenres()
            }
        }
        viewModel.listGenre.observe(viewLifecycleOwner) {
            showGenreTabs(it)
        }
        viewModel.listBuku.observe(viewLifecycleOwner) {
            showSearchResult(it)
        }
        viewModel.isShowTabs.observe(viewLifecycleOwner) {
            showTabs(it)
        }
    }

    private fun showGenreTabs(listGenre: List<String>) {
        if (listGenre.isNotEmpty()) {
            binding.tvNoData.visibility = View.GONE

            val listTabGenre = listGenre.map { genre -> TabGenreData(genre) }
            val adapter = SectionsPagerAdapter(this, listTabGenre)
            binding.viewPager.adapter = adapter

            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                tab.text = listTabGenre[position].genre
            }.attach()
        } else {
            binding.tvNoData.visibility = View.VISIBLE
        }
    }

    private fun showSearchResult(listBook: List<BookItem>) {
        if (listBook.isNotEmpty()) {
            val adapter = BookAdapter()
            adapter.submitList(listBook)

            adapter.setOnItemClickCallback(object : BookAdapter.OnItemClickCallback {
                override fun onItemClicked(data: BookItem) {
                    val intent = Intent(activity, BookDetailActivity::class.java)
                    intent.putExtra(BookDetailActivity.EXTRA_ID, data.booksId)
                    startActivity(intent)
                }
            })
            binding.rvBook.adapter = adapter
        }
    }

    private fun showTabs(isShowTab: Boolean) {
        if (isShowTab) {
            binding.rvBook.visibility = View.GONE
            binding.viewPager.visibility = View.VISIBLE
            binding.tabs.visibility = View.VISIBLE
        } else {
            binding.rvBook.visibility = View.VISIBLE
            binding.viewPager.visibility = View.GONE
            binding.tabs.visibility = View.GONE
        }
    }

    private fun search() {
        val searchText = binding.edSearch.text.toString()

        if (searchText.isNotBlank()) {
            viewModel.search(searchText)
        } else {
            viewModel.getGenres()
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