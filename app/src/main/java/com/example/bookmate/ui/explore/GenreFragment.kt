package com.example.bookmate.ui.explore

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.R
import com.example.bookmate.data.response.BookItem
import com.example.bookmate.databinding.FragmentGenreBinding
import com.example.bookmate.ui.bookdetail.BookDetailActivity
import com.example.bookmate.ui.home.BookAdapter
import com.example.bookmate.ui.home.HomeViewModel
import com.example.bookmate.utils.ViewModelFactory

class GenreFragment : Fragment() {
    private lateinit var binding: FragmentGenreBinding
    private lateinit var viewModel: GenreViewModel

    companion object {
        private const val EXTRA_GENRE = "content"

        fun newInstance(genre: String): GenreFragment {
            val fragment = GenreFragment()
            val args = Bundle()
            args.putString(EXTRA_GENRE, genre)
            fragment.arguments = args
            return fragment
        }
    }

    private var genre: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            genre = it.getString(EXTRA_GENRE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGenreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = obtainViewModel(this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    private fun setupObserver() {
        val genreValue = genre
        if (!genreValue.isNullOrBlank()) {
            viewModel.getData(genreValue)
        } else {
            showToast("Couldn't get genre data")
        }

        viewModel.isErrorGetData.observe(viewLifecycleOwner) {
            if (it) {
                showToast(viewModel.getErrorMessage())
            }
        }
        viewModel.listBuku.observe(viewLifecycleOwner) {
            showBookData(it)
        }
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

    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(fragment: Fragment): GenreViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireContext())
        return ViewModelProvider(fragment, factory)[GenreViewModel::class.java]
    }
}