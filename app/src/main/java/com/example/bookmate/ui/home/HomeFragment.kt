package com.example.bookmate.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.components.SearchField
import com.example.bookmate.data.response.BookItem
import com.example.bookmate.databinding.FragmentHomeBinding
import com.example.bookmate.ui.bookdetail.BookDetailActivity
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

    override fun onResume() {
        super.onResume()
        search()
    }

    private fun setupObserver() {
        search()

        viewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        viewModel.pageTitle.observe(requireActivity()) {
            binding.tvTitle.text = it
        }

        viewModel.listBuku.observe(requireActivity()) {
            if (it.isEmpty()) {
                binding.tvNoData.visibility = View.VISIBLE
                binding.rvBook.visibility = View.GONE
            } else {
                binding.tvNoData.visibility = View.GONE
                binding.rvBook.visibility = View.VISIBLE
                showGenreData(it)
            }
        }

        viewModel.isErrorGetData.observe(requireActivity()) {
            if (it) {
                showToast(viewModel.getErrorMessage())
            }
        }
    }

    private fun setupAction() {
        binding.edSearch.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_SEARCH ||
                keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.edSearch.windowToken, 0)

                search()

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.edSearch.setOnItemClickCallback(object: SearchField.OnItemClickCallback {
            override fun onItemClicked() {
                viewModel.getData()
            }
        })
    }

    private fun search() {
        val searchText = binding.edSearch.text.toString()

        if (searchText.isNotBlank()) {
            viewModel.search(searchText)
        } else {
            viewModel.getData()
        }
    }

    private fun showGenreData(listBuku: List<BookItem>) {
        val adapter = BookAdapter()
        adapter.submitList(listBuku)
        adapter.setOnItemClickCallback(object : BookAdapter.OnItemClickCallback {
            override fun onItemClicked(data: BookItem) {
                val intent = Intent(activity, BookDetailActivity::class.java)
                intent.putExtra(BookDetailActivity.EXTRA_ID, data.booksId)
                startActivity(intent)
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