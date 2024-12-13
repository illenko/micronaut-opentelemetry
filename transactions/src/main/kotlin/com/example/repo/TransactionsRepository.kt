package com.example.repo

import com.example.model.Transaction
import jakarta.inject.Singleton

@Singleton
class TransactionsRepository {
    fun findById(id: Long): Transaction? = transactions.find { it.id == id }

    companion object {
        private val transactions =
            mutableListOf(
                Transaction(1, 100.0, "Lunch", 1),
                Transaction(2, 200.0, "Dinner", 1),
                Transaction(3, 300.0, "Breakfast", 2),
                Transaction(4, 400.0, "Snack", 2),
                Transaction(5, 500.0, "Dessert", 3),
                Transaction(6, 600.0, "Drink", 3),
                Transaction(7, 700.0, "Coffee", 4),
                Transaction(8, 800.0, "Tea", 4),
                Transaction(9, 900.0, "Water", 5),
                Transaction(10, 1000.0, "Juice", 6),
            )
    }
}
