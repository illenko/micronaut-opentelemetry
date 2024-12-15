package com.example.model

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@MappedEntity
data class Transaction(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,
    val amount: Double,
    val category: String,
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
    val category: String,
    val description: String,
    val user: User,
)

@Serdeable
data class CreateTransactionRequest(
    val amount: Double,
    val category: String,
    val description: String,
    val userId: Long,
)
