package com.example.model

import io.micronaut.serde.annotation.Serdeable

data class Transaction(
    val id: Long,
    val amount: Double,
    val description: String,
    val userId: Long,
)

@Serdeable
data class User(
    val id: Long,
    val name: String,
)

@Serdeable
data class TransactionResponse(
    val id: Long,
    val amount: Double,
    val description: String,
    val user: User,
)
