package com.flaviovizza.androidtodos.data.model

/**
 * Represents the structure of an error response returned by an API.
 *
 * @property error A description of the error.
 * @property code The error code associated with the error.
 */
data class ErrorResponse(
    val error: String,
    val code: Int
)
