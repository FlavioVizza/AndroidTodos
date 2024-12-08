package com.flaviovizza.androidtodos.data.model

/**
 * Represents a request to register a new user.
 *
 * @property username The desired username for the new user.
 * @property email The email address for the new user.
 * @property password The desired password for the new user.
 */
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)
