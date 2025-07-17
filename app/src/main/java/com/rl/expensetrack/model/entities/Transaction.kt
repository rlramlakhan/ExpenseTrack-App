package com.rl.expensetrack.model.entities

data class Transaction(
    val id: String,
    val title: String,
    val amount: String,
    val type: String,
    val uid: String
)
