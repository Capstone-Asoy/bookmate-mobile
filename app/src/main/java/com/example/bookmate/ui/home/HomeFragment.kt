package com.example.bookmate.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.databinding.FragmentHomeBinding
import com.example.bookmate.utils.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = obtainViewModel(this)
        return root
    }

    private fun obtainViewModel(fragment: Fragment): HomeViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireContext())
        return ViewModelProvider(fragment, factory)[HomeViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}