package com.flaviovizza.androidtodos.data.model

/**
 * Represents a response containing authentication tokens.
 *
 * @property accessToken The access token used for authenticated requests.
 * @property refreshToken The refresh token used to obtain a new access token.
 */
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
