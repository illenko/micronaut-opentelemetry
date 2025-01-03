package com.example.repo

import com.example.model.Transaction
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface TransactionsRepository : CrudRepository<Transaction, Long>
