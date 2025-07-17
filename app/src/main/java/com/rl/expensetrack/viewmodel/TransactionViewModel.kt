package com.rl.expensetrack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rl.expensetrack.model.entities.Transaction
import com.rl.expensetrack.model.repositories.TransactionRepository

class TransactionViewModel(private val transactionRepository: TransactionRepository): ViewModel() {

    private val _transactionResult = MutableLiveData<Result<Boolean>>()
    val transactionResult: LiveData<Result<Boolean>> = _transactionResult

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    fun addTransaction(transaction: Transaction) {
        transactionRepository.addTransaction(transaction).observeForever {
            _transactionResult.value = it
        }
    }

    fun getTransactions() {
        transactionRepository.getTransactions().observeForever {
            _transactions.value = it
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        transactionRepository.deleteTransaction(transaction)
    }

}

class TransactionViewModelFactory(private val transactionRepository: TransactionRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionViewModel(transactionRepository) as T
    }
}