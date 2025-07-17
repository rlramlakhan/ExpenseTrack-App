package com.rl.expensetrack.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rl.expensetrack.R
import com.rl.expensetrack.databinding.FragmentHomeBinding
import com.rl.expensetrack.model.repositories.TransactionRepository
import com.rl.expensetrack.view.activities.MainActivity
import com.rl.expensetrack.view.adapters.TransactionAdapter
import com.rl.expensetrack.viewmodel.TransactionViewModel
import com.rl.expensetrack.viewmodel.TransactionViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val transactionRepository = TransactionRepository()
        val transactionFactory = TransactionViewModelFactory(transactionRepository)
        transactionViewModel = ViewModelProvider(this, transactionFactory)[TransactionViewModel::class.java]
        transactionAdapter = TransactionAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = transactionAdapter

        transactionViewModel.getTransactions()
        observeViewModel()

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val transaction = transactionAdapter.list[position]

                AlertDialog.Builder(requireContext())
                    .setTitle("Delete Transaction")
                    .setMessage("Are you sure you want to delete this transaction?")
                    .setPositiveButton("Delete") {_, _ ->
                        transactionViewModel.deleteTransaction(transaction)
                    }
                    .setNeutralButton("Cancel") {dialog, _ ->
                        transactionAdapter.notifyItemChanged(position)
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            }

        }

        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerView)

        binding.fabBtnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_transactionFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        transactionViewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            if (transactions.isEmpty()) {
                binding.tvNoTransactions.visibility = View.VISIBLE
            } else {
                binding.tvNoTransactions.visibility = View.GONE
            }
            transactionAdapter.updateList(transactions)
            var income = 0
            var expense = 0
            transactions.forEach {
                when(it.type) {
                    "Income" -> income += it.amount.toInt()
                    "Expense" -> expense += it.amount.toInt()
                }
            }
            binding.tvBalanceAmount.text = "$${(income - expense)}"
            binding.tvIncomeAmount.text = "+$$income"
            binding.tvExpenseAmount.text = "-$$expense"
            binding.progressBarHome.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}