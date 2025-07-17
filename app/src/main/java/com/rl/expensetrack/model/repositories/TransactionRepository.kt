package com.rl.expensetrack.model.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rl.expensetrack.model.entities.Transaction

class TransactionRepository {
    private val database = FirebaseDatabase.getInstance().getReference("transactions")

    private val authRepository = AuthRepository()

    fun addTransaction(transaction: Transaction): LiveData<Result<Boolean>> {
        val result = MutableLiveData<Result<Boolean>>()

        database.child(transaction.id).setValue(transaction)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.value = Result.success(true)
                } else {
                    result.value = Result.failure(task.exception ?: Exception(task.exception?.message))
                }
            }
        return result
    }

    fun getTransactions(): LiveData<List<Transaction>> {
        val list = MutableLiveData<List<Transaction>>()

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transactionList = mutableListOf<Transaction>()
                for (data in snapshot.children) {
                    if (data.child("uid").value.toString() == authRepository.getCurrentUser()?.uid.toString()) {
                        val id = data.child("id").value.toString()
                        val title = data.child("title").value.toString()
                        val amount = data.child("amount").value.toString()
                        val type = data.child("type").value.toString()
                        val uid = data.child("uid").value.toString()
                        val transaction = Transaction(id, title, amount, type, uid)
                        transactionList.add(transaction)
                        list.value = transactionList
                    } else {
                        list.value = emptyList()
                    }
                }
                if (!snapshot.exists()) {
                    list.value = emptyList()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                list.value = emptyList()
            }

        })
        return list
    }

    fun deleteTransaction(transaction: Transaction) {
        database.child(transaction.id).removeValue()
    }
}