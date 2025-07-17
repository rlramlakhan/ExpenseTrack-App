package com.rl.expensetrack.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rl.expensetrack.R
import com.rl.expensetrack.model.entities.Transaction

class TransactionAdapter: RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private var transactions: List<Transaction> = emptyList()
    val list: List<Transaction> get() = transactions
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_each_transaction, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.tvTitle.text = transaction.title
        if (transaction.type == "Income") {
            holder.tvAmount.text = "+$${transaction.amount}"
        } else {
            holder.tvAmount.text = "-$${transaction.amount}"
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(transactions: List<Transaction>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }
}