package com.flaviovizza.androidtodos.data.model

/**
 * Represents the structure of a generic response returned by an API.
 *
 * @property success .
 * @property message The message associated with the response.
 */
data class GenericResponse(
    val success: Boolean,
    val message: String
)
