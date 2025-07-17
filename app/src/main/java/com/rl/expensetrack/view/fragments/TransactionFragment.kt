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
import com.rl.expensetrack.databinding.FragmentTransactionBinding
import com.rl.expensetrack.model.entities.Transaction
import com.rl.expensetrack.model.repositories.AuthRepository
import com.rl.expensetrack.model.repositories.TransactionRepository
import com.rl.expensetrack.view.activities.MainActivity
import com.rl.expensetrack.viewmodel.AuthViewModel
import com.rl.expensetrack.viewmodel.AuthViewModelFactory
import com.rl.expensetrack.viewmodel.TransactionViewModel
import com.rl.expensetrack.viewmodel.TransactionViewModelFactory

class TransactionFragment : Fragment() {
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        val transactionRepository = TransactionRepository()
        val transactionFactory = TransactionViewModelFactory(transactionRepository)
        transactionViewModel = ViewModelProvider(this, transactionFactory)[TransactionViewModel::class.java]

        val authRepository = AuthRepository()
        val authFactory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, authFactory)[AuthViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val amount = binding.etAmount.text.toString()
            val type = binding.spinner.selectedItem.toString()

            if (title.isEmpty() && amount.isEmpty()) {
                binding.etTitle.error = "Title is required"
                binding.etAmount.error = "Amount is required"
            }
            if (title.isEmpty()) {
                binding.etTitle.error = "Title is required"
            }
            if (amount.isEmpty()) {
                binding.etAmount.error = "Amount is required"
            }

            if (title.isNotEmpty() && amount.isNotEmpty()) {
                val id = System.currentTimeMillis().toString()
                val uid = authViewModel.getCurrentUser()?.uid.toString()
                val transaction = Transaction(id, title, amount, type, uid)
                transactionViewModel.addTransaction(transaction)
            }
        }
    }

    private fun observeViewModel() {
        transactionViewModel.transactionResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
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