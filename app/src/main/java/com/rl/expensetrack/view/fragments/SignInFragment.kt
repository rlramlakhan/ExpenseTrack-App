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
import com.rl.expensetrack.databinding.FragmentSignInBinding
import com.rl.expensetrack.model.repositories.AuthRepository
import com.rl.expensetrack.view.activities.MainActivity
import com.rl.expensetrack.viewmodel.AuthViewModel
import com.rl.expensetrack.viewmodel.AuthViewModelFactory

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val authRepository = AuthRepository()
        val authFactory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, authFactory)[AuthViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        binding.ibBackSignIn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmailSignIn.text.toString()
            val password = binding.etPasswordSignIn.text.toString()

            if (email.isEmpty() && password.isEmpty()) {
                binding.etEmailSignIn.error = "Email is required"
                binding.etPasswordSignIn.error = "Password is required"
            }
            if (email.isEmpty()) {
                binding.etEmailSignIn.error = "Email is required"
            }
            if (password.isEmpty()) {
                binding.etPasswordSignIn.error = "Password is required"
            }

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.signIn(email, password)
                binding.progressBarSignIn.visibility = View.VISIBLE
            }
        }
    }

    private fun observeViewModel() {
        authViewModel.authResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            result.onFailure { e ->
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
            binding.progressBarSignIn.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}