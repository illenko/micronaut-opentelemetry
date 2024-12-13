package com.example.api

import com.example.client.UsersClient
import com.example.model.TransactionResponse
import com.example.repo.TransactionsRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
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
        return repo.findById(id)?.let { transaction ->
            log.info("Transaction found: $transaction")

            log.info("Fetching user with id: ${transaction.userId}")
            val user = usersClient.userById(transaction.userId)

            user?.let {
                log.info("User found: $user")
                HttpResponse.ok(
                    TransactionResponse(
                        transaction.id,
                        transaction.amount,
                        transaction.description,
                        user,
                    ),
                )
            } ?: run {
                log.warn("User with id: ${transaction.userId} not found")
                HttpResponse.serverError()
            }
        } ?: run {
            log.warn("Transaction with id: $id not found")
            HttpResponse.notFound()
        }
    }
}
