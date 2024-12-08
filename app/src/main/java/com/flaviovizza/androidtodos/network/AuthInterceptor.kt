package com.flaviovizza.androidtodos.network

import com.flaviovizza.androidtodos.data.StorageRepository
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An OkHttp interceptor that adds an Authorization header with a Bearer token to HTTP requests.
 * This interceptor retrieves the access token from the storage repository and appends it to the
 * request headers to authenticate API requests.
 *
 * @param storage The [StorageRepository] used to retrieve the access token.
 */
class AuthInterceptor(private val storage: StorageRepository) : Interceptor {

    /**
     * Intercepts the request and adds the Authorization header with the Bearer token.
     *
     * This method retrieves the access token from the [StorageRepository] and adds it to the
     * request headers as a "Bearer" token for authentication purposes.
     *
     * @param chain The interceptor chain that allows you to modify the request before proceeding.
     * @return The modified [Response] after proceeding with the new request.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = storage.getData(StorageRepository.StorageKeys.ACCESS_TOKEN)

        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }

}
