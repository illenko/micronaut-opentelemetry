package com.example.api

import com.example.model.User
import com.example.repo.UsersRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory

@Controller("/users")
class UsersController(
    private val repo: UsersRepository,
) {
    private val log = LoggerFactory.getLogger(UsersController::class.java)

    @Get("/{id}")
    fun userById(id: Long): HttpResponse<User> {
        log.info("Fetching user with id: $id")
        return repo.findById(id)?.let { user ->
            log.info("User found: $user")
            HttpResponse.ok(user)
        } ?: run {
            log.warn("User with id: $id not found")
            HttpResponse.notFound()
        }
    }
}
