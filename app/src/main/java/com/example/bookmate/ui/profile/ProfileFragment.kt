package com.example.bookmate.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bookmate.databinding.FragmentProfileBinding
import com.example.bookmate.ui.login.LoginActivity
import com.example.bookmate.utils.ViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
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
        viewModel.isLoading.observe(requireActivity()) {}

        viewModel.user.observe(requireActivity()) {
            binding.tvEmail.text = it.email
            binding.tvName.text = it.name
            if (it.photoUrl.isNotBlank()) {
                Glide.with(binding.root.context).load(it.photoUrl).into(binding.imgProfile)
            }
        }

        viewModel.profileData.observe(requireActivity()) {
            binding.tvReadingList.text = it.readingList.toString()
            binding.tvBookReviewed.text = it.listRating.toString()
        }
    }

    private fun setupAction() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()

            startActivity(Intent(activity, LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun obtainViewModel(fragment: Fragment): ProfileViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireContext())
        return ViewModelProvider(fragment, factory)[ProfileViewModel::class.java]
    }
}