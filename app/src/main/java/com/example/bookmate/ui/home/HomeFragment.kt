package com.example.bookmate.ui.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookmate.R
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

        binding.header.clipToOutline = true

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun setupObserver() {
        getData()

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.pageTitle.observe(viewLifecycleOwner) {
            binding.tvTitle.text = it
        }

        viewModel.listBuku.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.tvNoData.visibility = View.VISIBLE
                binding.rvBook.visibility = View.GONE
            } else {
                binding.tvNoData.visibility = View.GONE
                binding.rvBook.visibility = View.VISIBLE
                showBookData(it)
            }
        }
        viewModel.listBuku2.observe(viewLifecycleOwner) {
            showBookData2(it)
        }
        viewModel.listBukuFromHistory.observe(viewLifecycleOwner) {
            showBookDataFromHistory(it)
        }
        viewModel.listBukuFromBookmark.observe(viewLifecycleOwner) {
            showBookDataFromBookmark(it)
        }

        viewModel.isErrorGetData.observe(viewLifecycleOwner) {
            if (it) {
                showToast(viewModel.getErrorMessage())
            }
        }
    }

    private fun setupAction() {

    }

    private fun getData() {
        viewModel.getData()
    }

    private fun showBookData(listBuku: List<BookItem>) {
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

    private fun showBookData2(listBuku: List<BookItem>) {
        if (listBuku.isNotEmpty()) {
            binding.tvForYou2.visibility = View.VISIBLE
            binding.rvBook2.visibility = View.VISIBLE

            val adapter = BookAdapter()
            adapter.submitList(listBuku)
            adapter.setOnItemClickCallback(object : BookAdapter.OnItemClickCallback {
                override fun onItemClicked(data: BookItem) {
                    val intent = Intent(activity, BookDetailActivity::class.java)
                    intent.putExtra(BookDetailActivity.EXTRA_ID, data.booksId)
                    startActivity(intent)
                }
            })
            binding.rvBook2.adapter = adapter
        } else {
            binding.tvForYou2.visibility = View.GONE
            binding.rvBook2.visibility = View.GONE
        }
    }

    private fun showBookDataFromHistory(listBuku: List<BookItem>) {
        if (listBuku.isNotEmpty()) {
            binding.rvBookFromHistory.visibility = View.VISIBLE
            binding.tvBasedOnHistory.visibility = View.VISIBLE

            val adapter = BookAdapter()
            adapter.submitList(listBuku)
            adapter.setOnItemClickCallback(object : BookAdapter.OnItemClickCallback {
                override fun onItemClicked(data: BookItem) {
                    val intent = Intent(activity, BookDetailActivity::class.java)
                    intent.putExtra(BookDetailActivity.EXTRA_ID, data.booksId)
                    startActivity(intent)
                }
            })
            binding.rvBookFromHistory.adapter = adapter

        } else {
            binding.rvBookFromHistory.visibility = View.GONE
            binding.tvBasedOnHistory.visibility = View.GONE
        }
        setupView()
    }

    private fun showBookDataFromBookmark(listBuku: List<BookItem>) {
        if (listBuku.isNotEmpty()) {
            binding.rvBookFromBookmark.visibility = View.VISIBLE
            binding.tvBasedOnBookmark.visibility = View.VISIBLE

            val adapter = BookAdapter()
            adapter.submitList(listBuku)
            adapter.setOnItemClickCallback(object : BookAdapter.OnItemClickCallback {
                override fun onItemClicked(data: BookItem) {
                    val intent = Intent(activity, BookDetailActivity::class.java)
                    intent.putExtra(BookDetailActivity.EXTRA_ID, data.booksId)
                    startActivity(intent)
                }
            })
            binding.rvBookFromBookmark.adapter = adapter

        } else {
            binding.rvBookFromBookmark.visibility = View.GONE
            binding.tvBasedOnBookmark.visibility = View.GONE
        }
        setupView()
    }

    private fun setupView() {
        val listBookFromHistory = viewModel.getListBookFromHistory()
        val listBookFromBookmark = viewModel.getListBookFromBookmark()

        if (listBookFromHistory.isNotEmpty() && listBookFromBookmark.isNotEmpty()) {
            val layoutManager1 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvBook.layoutManager = layoutManager1
            val layoutManager2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvBook2.layoutManager = layoutManager2
        } else if (listBookFromHistory.isNotEmpty() || listBookFromBookmark.isNotEmpty()) {
            val layoutManager2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvBook2.layoutManager = layoutManager2
        } else {
            val layoutManager1 =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            binding.rvBook.layoutManager = layoutManager1
            val layoutManager2 =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            binding.rvBook2.layoutManager = layoutManager2
        }
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