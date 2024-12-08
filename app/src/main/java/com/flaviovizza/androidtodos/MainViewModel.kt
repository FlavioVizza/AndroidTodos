package com.flaviovizza.androidtodos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.flaviovizza.androidtodos.data.AppEventManager
import com.flaviovizza.androidtodos.data.ThemeRepository

import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * The [MainViewModel] class is responsible for managing the app's primary business logic
 * and serving as a bridge between the UI and the repositories for authentication and theme management.
 *
 * This ViewModel is annotated with [@HiltViewModel], enabling dependency injection via Hilt.
 *
 * @property appEventManager Manages application-wide events, such as authentication state changes.
 * @property themeRepo Handles theme-related logic, such as retrieving and setting the app's theme.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val appEventManager: AppEventManager,
    private val themeRepo: ThemeRepository,
) : ViewModel() {

    /**
     * A [LiveData] instance representing the current authentication state of the user.
     */
    val authState: LiveData<Boolean> = appEventManager.authState

    /**
     * Checks if the user is currently logged in.
     *
     * @return `true` if the user is logged in, `false` otherwise.
     */
    fun isUserLoggedIn() = appEventManager.isUserLoggedIn()

    /**
     * Retrieves the app's current theme setting.
     *
     * @return A string representing the app theme (e.g., "light", "dark", or "unspecified").
     */
    fun getAppTheme() = themeRepo.getAppTheme()

    /**
     * Updates the app's theme to the specified value.
     *
     * @param appTheme A string representing the desired app theme ("light", "dark", or "unspecified").
     */
    fun setAppTheme(appTheme: String) = themeRepo.setAppTheme(themeRepo.getThemeValue(appTheme))

    /**
     * Retrieves the system theme setting and converts it to a human-readable string.
     *
     * @return A string representing the system theme (e.g., "light" or "dark").
     */
    fun getSystemTheme(): String {
        val sysTheme = themeRepo.getSystemTheme()
        return themeRepo.getThemeAsString(sysTheme)
    }

}