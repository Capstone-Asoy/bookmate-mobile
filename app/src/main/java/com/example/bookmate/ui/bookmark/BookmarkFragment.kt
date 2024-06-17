package com.example.bookmate.ui.bookmark

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.data.response.BookmarksItem
import com.example.bookmate.databinding.FragmentBookmarkBinding
import com.example.bookmate.ui.bookdetail.BookDetailActivity
import com.example.bookmate.utils.ViewModelFactory

class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var viewModel: BookmarkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
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
        viewModel.getBookmarks()
    }

    private fun setupAction() {

    }

    private fun setupObserver() {
        viewModel.getBookmarks()

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        viewModel.isError.observe(viewLifecycleOwner) {
            if (it) {
                showToast(viewModel.getErrorMessage())
            }
        }
        viewModel.listBookmark.observe(viewLifecycleOwner) {
            showData(it)
        }
    }

    private fun showData(listBookmark: List<BookmarksItem>) {
        if (listBookmark.isEmpty()) {
            binding.tvNoBookmark.visibility = View.VISIBLE
            binding.rvBookmark.visibility = View.GONE
        } else {
            binding.tvNoBookmark.visibility = View.GONE
            binding.rvBookmark.visibility = View.VISIBLE

            val adapter = BookmarkAdapter()
            adapter.submitList(listBookmark)
            adapter.setOnItemClickCallback(object : BookmarkAdapter.OnItemClickCallback {
                override fun onItemClicked(data: BookmarksItem) {
                    val intent = Intent(activity, BookDetailActivity::class.java)
                    intent.putExtra(BookDetailActivity.EXTRA_ID, data.id)
                    startActivity(intent)
                }
            })

            binding.rvBookmark.adapter = adapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(fragment: Fragment): BookmarkViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireContext())
        return ViewModelProvider(fragment, factory)[BookmarkViewModel::class.java]
    }

}