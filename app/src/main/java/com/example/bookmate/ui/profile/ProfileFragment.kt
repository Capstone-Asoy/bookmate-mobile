package com.example.bookmate.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bookmate.R
import com.example.bookmate.databinding.FragmentProfileBinding
import com.example.bookmate.ui.login.LoginActivity
import com.example.bookmate.ui.welcome.WelcomeActivity
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

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }

    private fun setupObserver() {
        viewModel.getData()
        viewModel.isLoading.observe(viewLifecycleOwner) {}

        viewModel.user.observe(viewLifecycleOwner) {
            binding.tvEmail.text = it.email
            binding.tvName.text = it.name
            if (it.photoUrl.isNotBlank() && !it.photoUrl.endsWith(getString(R.string.no_image))) {
                Glide.with(binding.root.context).load(it.photoUrl).into(binding.imgProfile)
            }
        }

        viewModel.profileData.observe(viewLifecycleOwner) {
            binding.tvReadingList.text = it.readingList.toString()
            binding.tvBookReviewed.text = it.listRating.toString()

            if (it.listImage.isEmpty()) {
                binding.rvHistory.visibility = View.GONE
                binding.imgNoHistory.visibility = View.VISIBLE
                binding.tvNoHistory.visibility = View.VISIBLE
            } else {
                binding.rvHistory.visibility = View.VISIBLE
                binding.imgNoHistory.visibility = View.GONE
                binding.tvNoHistory.visibility = View.GONE

                val adapter = HistoryAdapter()
                adapter.submitList(it.listImage)
                binding.rvHistory.adapter = adapter
            }
        }
    }

    private fun setupAction() {
        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireActivity()).apply {
            setTitle(R.string.log_out)
            setMessage(R.string.logout_msg)
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(R.string.yes) { _, _ ->
                viewModel.logout()

                startActivity(Intent(activity, LoginActivity::class.java))
                requireActivity().finish()
            }
            create()
            show()
        }
    }

    private fun obtainViewModel(fragment: Fragment): ProfileViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireContext())
        return ViewModelProvider(fragment, factory)[ProfileViewModel::class.java]
    }
}