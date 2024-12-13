package com.example.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class User(
    val id: Long,
    val name: String,
)
