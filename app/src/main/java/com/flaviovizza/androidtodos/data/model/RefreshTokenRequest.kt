package com.flaviovizza.androidtodos.data.model

/**
 * Represents a request to refresh an authentication token.
 *
 * @property refreshToken The refresh token used to obtain a new access token.
 */
data class RefreshTokenRequest(
    val refreshToken: String
)
