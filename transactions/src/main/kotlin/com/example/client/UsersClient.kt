package com.example.client

import com.example.model.User
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client("users")
interface UsersClient {
    @Get("/users/{id}")
    fun userById(id: Long): User?
}
