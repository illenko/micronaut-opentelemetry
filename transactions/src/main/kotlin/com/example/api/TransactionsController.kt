package com.example.api

import com.example.client.UsersClient
import com.example.model.CreateTransactionRequest
import com.example.model.Transaction
import com.example.model.TransactionResponse
import com.example.repo.TransactionsRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import org.slf4j.LoggerFactory

@Controller("/transactions")
class TransactionsController(
    private val repo: TransactionsRepository,
    private val usersClient: UsersClient,
) {
    private val log = LoggerFactory.getLogger(TransactionsController::class.java)

    @Get("/{id}")
    fun transactionById(id: Long): HttpResponse<TransactionResponse> {
        log.info("Fetching transaction with id: $id")
        val transactionOpt = repo.findById(id)
        if (transactionOpt.isPresent) {
            val transaction = transactionOpt.get()
            log.info("Transaction found: $transaction")

            log.info("Fetching user with id: ${transaction.userId}")
            val user = usersClient.userById(transaction.userId)

            return user?.let {
                log.info("User found: $user")
                HttpResponse.ok(
                    TransactionResponse(
                        id = transaction.id!!,
                        amount = transaction.amount,
                        category = transaction.category,
                        description = transaction.description,
                        user,
                    ),
                )
            } ?: run {
                log.warn("User with id: ${transaction.userId} not found")
                HttpResponse.serverError()
            }
        } else {
            log.warn("Transaction with id: $id not found")
            return HttpResponse.notFound()
        }
    }

    @Post("/")
    fun create(
        @Body request: CreateTransactionRequest,
    ): HttpResponse<Transaction> {
        log.info("Creating new transaction: $request")
        val transaction =
            Transaction(
                amount = request.amount,
                category = request.category,
                description = request.description,
                userId = request.userId,
            )
        val savedTransaction = repo.save(transaction)
        log.info("Transaction created with id: ${savedTransaction.id}")
        return HttpResponse.created(savedTransaction)
    }
}
