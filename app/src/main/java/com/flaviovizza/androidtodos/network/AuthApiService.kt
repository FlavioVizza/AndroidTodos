package com.flaviovizza.androidtodos.network

import com.flaviovizza.androidtodos.data.model.GenericResponse
import com.flaviovizza.androidtodos.data.model.LoginRequest
import com.flaviovizza.androidtodos.data.model.RefreshTokenRequest
import com.flaviovizza.androidtodos.data.model.RegisterRequest
import com.flaviovizza.androidtodos.data.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API service interface for authentication-related endpoints.
 * Provides methods to perform login, registration, and token refresh operations.
 */
interface AuthApiService {

    /**
     * Sends a login request to the API with the provided credentials.
     *
     * @param request The login request containing the user credentials (email and password).
     * @return A `Response` containing a `TokenResponse` with the access and refresh tokens if the login is successful.
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>

    /**
     * Sends a registration request to the API to create a new user account.
     *
     * @param request The registration request containing the user details (username, email, and password).
     * @return A `Response` containing a `GenericResponse` that indicates whether the registration was successful.
     */
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<GenericResponse>

    /**
     * Sends a request to the API to refresh the user's access token using a valid refresh token.
     *
     * @param request The refresh token request.
     * @return A `Response` containing a `TokenResponse` with the new access and refresh tokens if successful.
     */
    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshTokenRequest): Response<TokenResponse>

}