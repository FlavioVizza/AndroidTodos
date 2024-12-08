package com.flaviovizza.androidtodos.data

import com.flaviovizza.androidtodos.data.model.LoginRequest
import com.flaviovizza.androidtodos.data.model.RefreshTokenRequest
import com.flaviovizza.androidtodos.data.model.RegisterRequest
import com.flaviovizza.androidtodos.data.model.TokenResponse
import com.flaviovizza.androidtodos.network.AuthApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A repository class responsible for interacting with authentication-related API endpoints.
 * This class uses Hilt for dependency injection and provides methods for login, registration, and token refresh.
 */
@Singleton
class AuthApiRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val storage: StorageRepository
){

    /**
     * Attempts to log in a user with the provided credentials.
     * If successful, the access token and refresh token are saved in local storage.
     *
     * @param request The login request containing user credentials (email and password).
     * @return `true` if login was successful and tokens were stored, `false` otherwise.
     */
    suspend fun login(request: LoginRequest) : Boolean {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            val result = authApiService.login(request)
            if (! result.isSuccessful) {false }
            else {
                val token = result.body()?.accessToken ?: ""
                val refreshToken = result.body()?.refreshToken ?: ""
                storage.saveData(StorageRepository.StorageKeys.ACCESS_TOKEN, token)
                storage.saveData(StorageRepository.StorageKeys.REFRESH_TOKEN, refreshToken)
                true
            }
        }
        return deferred.await()
    }

    /**
     * Attempts to register a new user with the provided information.
     *
     * @param request The registration request containing user details (username, email, password).
     * @return `true` if registration was successful, `false` otherwise.
     */
    suspend fun register(request: RegisterRequest) : Boolean {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            val result = authApiService.register(request)
            if (result.isSuccessful) { result.body()?.success ?: false }
            else false
        }
        return deferred.await()
    }

    /**
     * Attempts to refresh the authentication token using a provided refresh token.
     *
     * @param request The refresh token request containing the refresh token.
     * @return A `TokenResponse` containing new access and refresh tokens, or `null` if the refresh failed.
     */
    suspend fun refresh(request: RefreshTokenRequest) : TokenResponse? {
        return authApiService.refresh(request).body()
    }

}