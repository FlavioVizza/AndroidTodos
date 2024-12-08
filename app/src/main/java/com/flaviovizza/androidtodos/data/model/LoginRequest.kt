package com.flaviovizza.androidtodos.data.model

/**
 * Represents a request to log in a user.
 *
 * @property email The user's email address.
 * @property password The user's password.
 */
data class LoginRequest(
    val email: String,
    val password: String
)
