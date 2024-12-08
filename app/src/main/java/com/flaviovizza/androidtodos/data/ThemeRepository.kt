package com.flaviovizza.androidtodos.data

import android.content.res.Configuration
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
import androidx.navigation.fragment.findNavController
import com.flaviovizza.androidtodos.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A repository class responsible for managing the theme settings of the app.
 * This includes retrieving the system theme, app theme, and saving the user's preferred theme.
 * The class uses Hilt for dependency injection and is annotated as a Singleton.
 */
@Singleton
class ThemeRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storageRepo: StorageRepository
) {

    /**
     * Enum representing the possible theme values.
     * It defines three states: LIGHT, DARK, and UNSPECIFIED (for cases where no theme is set).
     */
    enum class ThemeValues(val value: Int) {
        LIGHT(1),
        DARK(2),
        UNSPECIFIED(99);
    }

    /**
     * Retrieves the current system theme (light or dark) based on the device's UI mode.
     *
     * @return An integer representing the system theme: `MODE_NIGHT_YES` for dark mode, `MODE_NIGHT_NO` for light mode.
     */
    fun getSystemTheme(): Int {
        return if ((context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
                MODE_NIGHT_YES
            else
                MODE_NIGHT_NO
    }

    /**
     * Retrieves the app's theme from local storage.
     * If no theme is saved, it returns the default theme as a string.
     *
     * @return The current app theme as a string ("light", "dark", or "unspecified").
     */
    fun getAppTheme(): String {
        val savedString = storageRepo.getData(StorageRepository.StorageKeys.APP_THEME)
        return getThemeAsString(savedString?.toInt() ?: MODE_NIGHT_UNSPECIFIED )
    }

    /**
     * Saves the specified theme as the app's preferred theme in both the app's settings and local storage.
     * It also applies the theme change immediately using `AppCompatDelegate`.
     *
     * @param theme The theme value to be set (`LIGHT`, `DARK`, or `UNSPECIFIED`).
     */
    fun setAppTheme(theme: ThemeValues){
        Log.i(javaClass.name, "saving new app theme $theme")
        AppCompatDelegate.setDefaultNightMode(theme.value)
        storageRepo.saveData(StorageRepository.StorageKeys.APP_THEME, theme.value.toString())
    }

    /**
     * Converts an integer theme value to its corresponding string representation.
     *
     * @param theme The integer theme value to convert (e.g., `MODE_NIGHT_YES`, `MODE_NIGHT_NO`).
     * @return A string representing the theme ("light", "dark", or "unspecified").
     */
    fun getThemeAsString( theme: Int ): String {
        return when(theme)  {
            MODE_NIGHT_NO ->  "light"
            MODE_NIGHT_YES ->  "dark"
            else -> "unspecified"
        }
    }

    /**
     * Converts a string theme value to its corresponding integer representation.
     *
     * @param theme A string representing a theme ("light", "dark", or "unspecified").
     * @return The corresponding integer theme value (`MODE_NIGHT_YES`, `MODE_NIGHT_NO`, or `MODE_NIGHT_UNSPECIFIED`).
     */
    fun getThemeAsInteger( theme: String ): Int {
        return when(theme.lowercase(Locale.ROOT))  {
            "light" -> MODE_NIGHT_NO
            "dark" -> MODE_NIGHT_YES
            else -> MODE_NIGHT_UNSPECIFIED
        }
    }

    /**
     * Converts a string theme value to its corresponding `ThemeValues` enum.
     *
     * @param from The string representing a theme ("light", "dark", or "unspecified").
     * @return The corresponding `ThemeValues` enum (`LIGHT`, `DARK`, or `UNSPECIFIED`).
     */
    fun getThemeValue(from: String): ThemeValues{
        return when(from.lowercase(Locale.ROOT))  {
            "light" -> ThemeValues.LIGHT
            "dark" -> ThemeValues.DARK
            else -> ThemeValues.UNSPECIFIED
        }
    }
}