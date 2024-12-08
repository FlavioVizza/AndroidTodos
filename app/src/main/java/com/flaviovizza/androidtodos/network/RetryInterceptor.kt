package com.flaviovizza.androidtodos.network

import com.flaviovizza.androidtodos.data.AppEventManager
import com.flaviovizza.androidtodos.data.StorageRepository
import com.flaviovizza.androidtodos.data.model.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An OkHttp interceptor that handles retries when receiving unauthorized (401) or forbidden (403) responses.
 * The interceptor attempts to refresh the access token using the stored refresh token, and retries
 * the request with a new access token if the refresh is successful. If the refresh fails, it notifies
 * the app that the user is no longer authenticated.
 *
 * @param storageRepository The [StorageRepository] used to retrieve and store the refresh and access tokens.
 * @param authApiService The [AuthApiService] used to refresh the access token.
 * @param appEventManager The [AppEventManager] used to notify the application about the authentication state.
 */
class RetryInterceptor(
        private val storageRepository: StorageRepository,
        private val authApiService: AuthApiService,
        private val appEventManager: AppEventManager
) : Interceptor {

    /**
     * Intercepts the HTTP request and retries it with a new access token if a 401 (Unauthorized)
     * or 403 (Forbidden) response is received. If refreshing the access token is successful,
     * the request is retried with the new token. If the refresh fails, the user is logged out.
     *
     * @param chain The interceptor chain that allows you to modify the request and response.
     * @return The response from the original or retried request.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalResponse = chain.proceed(originalRequest)

        if (originalResponse.code() == 401 || originalResponse.code() == 403) {
            originalResponse.close()
            val refreshToken = storageRepository.getData(StorageRepository.StorageKeys.REFRESH_TOKEN)

            if (refreshToken != null) {
                val newAccessToken = runBlocking {
                    val tokenResponse = authApiService.refresh(RefreshTokenRequest(refreshToken))
                    if (tokenResponse.isSuccessful && tokenResponse.body() != null) {
                        val refreshResponse = tokenResponse.body()!!
                        storageRepository.saveData(StorageRepository.StorageKeys.REFRESH_TOKEN, refreshResponse.refreshToken)
                        storageRepository.saveData(StorageRepository.StorageKeys.ACCESS_TOKEN, refreshResponse.accessToken)
                        refreshResponse.accessToken
                    } else {
                        null
                    }
                }

                if (newAccessToken == null) {
                    appEventManager.notifyAuthState(false)
                }
                else {
                    val newRequest = originalRequest.newBuilder().header(
                        "Authorization",
                        "Bearer $newAccessToken"
                    ).build()
                    return chain.proceed(newRequest)
                }
            }
            else {
                appEventManager.notifyAuthState(false)
            }
        }
        return originalResponse
    }

}
