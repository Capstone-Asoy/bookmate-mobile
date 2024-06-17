package com.example.bookmate.ui.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookmate.R

class GenreFragment : Fragment() {

    companion object {
        private const val EXTRA_GENRE = "content"

        fun newInstance(content: String): GenreFragment {
            val fragment = GenreFragment()
            val args = Bundle()
            args.putString(EXTRA_GENRE, content)
            fragment.arguments = args
            return fragment
        }
    }

    private var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            content = it.getString(EXTRA_GENRE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_genre, container, false)
    }
}