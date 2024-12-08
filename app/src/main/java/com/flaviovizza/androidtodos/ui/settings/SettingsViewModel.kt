package com.flaviovizza.androidtodos.ui.settings

import androidx.lifecycle.ViewModel
import com.flaviovizza.androidtodos.data.AppEventManager
import com.flaviovizza.androidtodos.data.LanguageRepository
import com.flaviovizza.androidtodos.data.StorageRepository
import com.flaviovizza.androidtodos.data.ThemeRepository
import com.flaviovizza.androidtodos.data.ThemeRepository.ThemeValues.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel that manages the logic for application settings, including theme management, language settings,
 * and user logout functionality. It interacts with repositories for managing themes, languages, and user data.
 *
 * @property storage The [StorageRepository] used for storing and retrieving app data, such as tokens.
 * @property theme The [ThemeRepository] responsible for managing the app's theme settings.
 * @property language The [LanguageRepository] for handling the user's language preferences.
 * @property appEventManager The [AppEventManager] used for managing application-level events, such as authentication state.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val storage: StorageRepository,
    private val theme: ThemeRepository,
    private val language: LanguageRepository,
    private val appEventManager: AppEventManager
) : ViewModel() {

    /**
     * Retrieves the current theme setting of the application.
     *
     * @return The current theme as a string, representing the theme's identifier.
     */
    fun getActualTheme(): String { return theme.getAppTheme() }

    /**
     * Checks whether the current theme is dark.
     *
     * @param themeIntValue The string value representing the current theme setting.
     *
     * @return A boolean indicating whether the theme is dark. Returns `true` if the theme is dark, otherwise `false`.
     */
    fun isDarkTheme(themeIntValue: String): Boolean{
        return theme.getThemeAsInteger(themeIntValue) == DARK.value
    }

    /**
     * Sets the application theme to either dark or light based on the provided boolean value.
     *
     * @param isDark A boolean indicating whether the dark theme should be applied (`true` for dark, `false` for light).
     */
    fun setAppTheme(isDark: Boolean){
        val selectedMode = if (isDark) DARK else LIGHT
        theme.setAppTheme(selectedMode)
    }

    /**
     * Logs the user out by notifying the app event manager.
     */
    fun logout(){
        appEventManager.notifyAuthState(false)
    }

    /**
     * Changes the app's language by setting the user's preferred language.
     *
     * @param ln The language code to set for the app.
     */
    fun changeLanguage(ln: String){ language.setUserLanguage(ln) }

    /**
     * Retrieves the list of available languages for the app.
     *
     * @return A list of resource IDs representing the available languages.
     */
    fun getAppLanguages() = LanguageRepository.getAppLanguages()

    /**
     * Retrieves the language code corresponding to a specific resource ID.
     *
     * @param resourceId The resource ID representing a language.
     *
     * @return A string representing the language code for the given resource ID.
     */
    fun getLanguageCodeBy(resourceId: Int) = LanguageRepository.getLanguageCodeBy(resourceId)

}