package com.flaviovizza.androidtodos

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext

import android.content.Context
import com.flaviovizza.androidtodos.data.AppEventManager
import javax.inject.Singleton

import com.flaviovizza.androidtodos.data.LanguageRepository
import com.flaviovizza.androidtodos.data.StorageRepository
import com.flaviovizza.androidtodos.data.ThemeRepository

/**
 * A Dagger Hilt module that provides the application's core dependencies.
 * This module is installed in the [SingletonComponent], meaning that all provided dependencies
 * will have a singleton scope and will be available throughout the app's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides a singleton instance of [StorageRepository], which is responsible for accessing and
     * managing the app's shared preferences and storing data locally.
     *
     * @param context The [Context] used to initialize the shared preferences.
     * @return A singleton instance of [StorageRepository].
     */
    @Provides
    @Singleton
    fun provideStorageRepository(
        @ApplicationContext context: Context
    ): StorageRepository {
        return StorageRepository(context)
    }

    /**
     * Provides a singleton instance of [AppEventManager], which is responsible for managing the app's
     * event notifications, such as authentication state changes and other global app events.
     *
     * @param storageRepo A [StorageRepository] instance used for accessing persistent storage.
     * @return A singleton instance of [AppEventManager].
     */
    @Provides
    @Singleton
    fun provideAppEventManager(
        storageRepo: StorageRepository
    ): AppEventManager {
        return AppEventManager(storageRepo)
    }

    /**
     * Provides a singleton instance of [ThemeRepository], which is responsible for managing the
     * app's theme settings, including saving and retrieving the current theme.
     *
     * @param context A [Context] instance used to access system resources for theme configuration.
     * @param storageRepository A [StorageRepository] instance used to store and retrieve theme preferences.
     * @return A singleton instance of [ThemeRepository].
     */
    @Provides
    @Singleton
    fun provideThemeRepository(
        @ApplicationContext context: Context,
        storageRepository: StorageRepository
    ): ThemeRepository {
        return ThemeRepository(context, storageRepository)
    }

    /**
     * Provides a singleton instance of [LanguageRepository], which is responsible for managing
     * the app's language settings, including saving and retrieving the selected language.
     *
     * @param context A [Context] instance used to access system resources for language configuration.
     * @param storageRepository A [StorageRepository] instance used to store and retrieve language preferences.
     * @return A singleton instance of [LanguageRepository].
     */
    @Provides
    @Singleton
    fun provideLanguageRepository(
        @ApplicationContext context: Context,
        storageRepository: StorageRepository
    ): LanguageRepository {
        return  LanguageRepository(context, storageRepository)
    }
}