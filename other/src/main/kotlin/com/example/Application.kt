package com.example

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.Micronaut.run
import io.micronaut.runtime.event.ApplicationStartupEvent
import io.micronaut.serde.annotation.Serdeable
import jakarta.inject.Singleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import kotlin.random.Random

fun main(args: Array<String>) {
    run(*args)
}

@Singleton
class Application(
    private val usersClient: UsersClient,
    private val transactionsClient: TransactionsClient,
) : ApplicationEventListener<ApplicationStartupEvent> {
    private val log = LoggerFactory.getLogger(Application::class.java)

    override fun onApplicationEvent(event: ApplicationStartupEvent) {
        runBlocking {
            val totalRequests = 10_000
            val parallelism = 5
            val categories = listOf("Utilities", "Groceries", "Entertainment", "Travel", "Misc")
            val descriptions =
                listOf(
                    "Monthly bill payment",
                    "Grocery shopping",
                    "Movie ticket",
                    "Flight booking",
                    "Random transaction",
                )
            val jobs =
                List(parallelism) {
                    launch {
                        repeat(totalRequests / parallelism) {
                            val userId = Random.nextLong(1, 7)
                            val transactionId = Random.nextLong(1, 21)

                            val user =
                                try {
                                    usersClient.userById(userId)
                                } catch (e: Exception) {
                                    log.error("Error fetching user with id: $userId", e)
                                    null
                                }

                            val transaction =
                                try {
                                    transactionsClient.transactionById(transactionId)
                                } catch (e: Exception) {
                                    log.error("Error fetching transaction with id: $transactionId", e)
                                    null
                                }

                            if (user != null && transaction != null) {
                                log.info("User: $user")
                                log.info("Transaction: $transaction")

                                if (Random.nextBoolean()) {
                                    val newTransaction =
                                        CreateTransactionRequest(
                                            amount = Random.nextDouble(1.0, 100.0),
                                            category = categories.random(),
                                            description = descriptions.random(),
                                            userId = userId,
                                        )
                                    try {
                                        transactionsClient.createTransaction(newTransaction)
                                        log.info("Created new transaction: $newTransaction")
                                    } catch (e: Exception) {
                                        log.error("Error creating transaction: $newTransaction", e)
                                    }
                                }
                            }

                            delay(1000)
                        }
                    }
                }
            jobs.joinAll()
        }
    }
}

@Client("transactions")
interface TransactionsClient {
    @Get("/transactions/{id}")
    fun transactionById(id: Long): TransactionResponse?

    @Post("/transactions")
    fun createTransaction(request: CreateTransactionRequest): TransactionResponse?
}

@Client("users")
interface UsersClient {
    @Get("/users/{id}")
    fun userById(id: Long): User?
}

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
