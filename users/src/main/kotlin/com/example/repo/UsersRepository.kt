package com.example.repo

import com.example.model.User
import jakarta.inject.Singleton

@Singleton
class UsersRepository {
    fun findById(id: Long): User? = users.find { it.id == id }

    companion object {
        private val users =
            mutableListOf(
                User(1, "Alice"),
                User(2, "Bob"),
                User(3, "Charlie"),
                User(4, "David"),
                User(5, "Eve"),
            )
    }
}
