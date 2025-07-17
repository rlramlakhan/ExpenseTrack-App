package com.rl.expensetrack.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rl.expensetrack.R
import com.rl.expensetrack.databinding.FragmentForgotPasswordBinding
import com.rl.expensetrack.model.repositories.AuthRepository
import com.rl.expensetrack.view.activities.MainActivity
import com.rl.expensetrack.viewmodel.AuthViewModel
import com.rl.expensetrack.viewmodel.AuthViewModelFactory

class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        val authRepository = AuthRepository()
        val authFactory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, authFactory)[AuthViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        binding.ibBackForgotPass.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSend.setOnClickListener {
            val email = binding.etEmailForgotPass.text.toString()

            if (email.isEmpty()) {
                binding.etEmailForgotPass.error = "Email is required"
            }

            if (email.isNotEmpty()) {
                authViewModel.resetPassword(email)
            }
        }
    }

    private fun observeViewModel() {
        authViewModel.resetLinkResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Email sent", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
            result.onFailure { e ->
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}