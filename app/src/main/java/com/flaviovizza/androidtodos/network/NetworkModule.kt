package com.flaviovizza.androidtodos.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.flaviovizza.androidtodos.BuildConfig
import com.flaviovizza.androidtodos.data.AppEventManager
import com.flaviovizza.androidtodos.data.AuthApiRepository
import com.flaviovizza.androidtodos.data.StorageRepository
import com.flaviovizza.androidtodos.data.TodoApiRepository
import okhttp3.OkHttpClient

/**
 * A Dagger module that provides all the necessary network-related dependencies for the application.
 * This module includes the creation of Retrofit, OkHttpClient, API services, and repositories.
 * The module is installed in the SingletonComponent to provide singleton instances for the entire app.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of Retrofit configured with the base URL and Gson converter.
     * Retrofit is used for making network requests to the API.
     *
     * @return A singleton instance of [Retrofit].
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provides a singleton instance of [AuthInterceptor] used for adding the Authorization header
     * with the JWT token to outgoing requests.
     *
     * @param storage A [StorageRepository] used to access the saved JWT token.
     * @return A singleton instance of [AuthInterceptor].
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(storage: StorageRepository): AuthInterceptor {
        return AuthInterceptor(storage)
    }

    /**
     * Provides a singleton instance of [RetryInterceptor] used for handling token refresh logic.
     * This interceptor automatically attempts to refresh the access token if a 401 or 403 error occurs.
     *
     * @param storage A [StorageRepository] used to access the saved refresh token.
     * @param authApiService An instance of [AuthApiService] used to refresh the access token.
     * @param appEventManager An instance of [AppEventManager] used to notify authentication state changes.
     * @return A singleton instance of [RetryInterceptor].
     */
    @Provides
    @Singleton
    fun provideRetryInterceptor(
        storage: StorageRepository,
        authApiService: AuthApiService,
        appEventManager: AppEventManager
    ): RetryInterceptor {
        return RetryInterceptor(storage, authApiService, appEventManager)
    }

    /**
     * Provides a singleton instance of [OkHttpClient] configured with the [AuthInterceptor] and
     * [RetryInterceptor] to manage JWT token authentication and automatic token refresh.
     *
     * @param authInterceptor An instance of [AuthInterceptor] to add the authorization header.
     * @param retryInterceptor An instance of [RetryInterceptor] to handle token refresh logic.
     * @return A singleton instance of [OkHttpClient] configured with the interceptors.
     */
    @Provides
    @Singleton
    fun provideTodoOkHttpClient(
        authInterceptor: AuthInterceptor,
        retryInterceptor: RetryInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // Add JWT Token
            .addInterceptor(retryInterceptor) // Menage refresh token
            .build()
    }

    /**
     * Provides a singleton instance of [AuthApiService] used to interact with the authentication API.
     *
     * @param retrofit A singleton instance of [Retrofit] used to create the API service.
     * @return A singleton instance of [AuthApiService].
     * */
    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    /**
     * Provides a singleton instance of [TodoApiService] used to interact with the To-Do API.
     * The API service is configured with an [OkHttpClient] that handles JWT token authentication and refresh.
     *
     * @param todoOkHttpClient A singleton instance of [OkHttpClient] used for network requests.
     * @return A singleton instance of [TodoApiService].
     */
    @Provides
    @Singleton
    fun provideTodoApiService(todoOkHttpClient: OkHttpClient): TodoApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(todoOkHttpClient) // Use your OkHttpClient in order to Add Bearer Token and handle token refresh
            .build()
            .create(TodoApiService::class.java)
    }

    /**
     * Provides a singleton instance of [AuthApiRepository] used to handle authentication-related API calls.
     *
     * @param authApiService A singleton instance of [AuthApiService] used to interact with the authentication API.
     * @param storageRepository A singleton instance of [StorageRepository] used to store and retrieve authentication tokens.
     * @return A singleton instance of [AuthApiRepository].
     */
    @Provides
    @Singleton
    fun provideAuthApiRepository(
        authApiService: AuthApiService,
        storageRepository: StorageRepository
    ): AuthApiRepository {
        return AuthApiRepository(authApiService, storageRepository)
    }

    /**
     * Provides a singleton instance of [TodoApiRepository] used to handle To-Do related API calls.
     *
     * @param todoApiService A singleton instance of [TodoApiService] used to interact with the To-Do API.
     * @return A singleton instance of [TodoApiRepository].
     */
    @Provides
    @Singleton
    fun provideTodoApiRepository(
        todoApiService: TodoApiService,
    ): TodoApiRepository {
        return TodoApiRepository(todoApiService)
    }
}


