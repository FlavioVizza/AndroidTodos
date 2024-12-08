package com.flaviovizza.androidtodos.data


import android.content.Context
import com.flaviovizza.androidtodos.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A repository class responsible for managing language settings in the app.
 * This includes retrieving and setting the user's selected language and accessing string resources.
 * This class uses Hilt for dependency injection.
 */
@Singleton
class LanguageRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storageRepository: StorageRepository
) {

    /**
     * Enum representing the supported language codes for the app.
     * The two languages supported are Italian ("it") and English ("en").
     */
    enum class LanguageCode(val code: String) {
        IT("it"),
        EN("en"),
    }

    companion object {
        /**
         * The default language code for the app.
         * Default is English ("en").
         */
        val DEFAULT_LANGUAGE = LanguageCode.EN.code

        /**
         * Returns a list of available languages for the app as resource IDs.
         *
         * @return A list of language resource IDs.
         */
        fun getAppLanguages(): List<Int>{
            return listOf(R.string.en, R.string.it)
        }

        /**
         * Returns the language code associated with a given resource ID.
         *
         * @param resourceId The resource ID for a language string.
         * @return The corresponding language code (either "en" or "it").
         */
        fun getLanguageCodeBy(resourceId: Int): String{
            return when (resourceId) {
                R.string.en -> LanguageCode.EN.code
                R.string.it -> LanguageCode.IT.code
                else -> DEFAULT_LANGUAGE
            }
        }
    }

    /**
     * Retrieves a string resource by its resource ID.
     *
     * @param resId The resource ID of the string to retrieve.
     * @return The string associated with the given resource ID.
     */
    fun getString(resId: Int): String {
        return context.getString(resId)
    }

    /**
     * Retrieves the user's selected language from local storage.
     * If no language is selected, the default language is returned.
     *
     * @return The selected language code (either "en" or "it").
     */
    fun getUserLanguage(): String {
        return storageRepository.getData(StorageRepository.StorageKeys.SELECTED_LANG) ?: DEFAULT_LANGUAGE
    }

    /**
     * Sets the user's selected language in local storage.
     *
     * @param language The language code to set (either "en" or "it").
     */
    fun setUserLanguage(language: String) {
        storageRepository.saveData(StorageRepository.StorageKeys.SELECTED_LANG, language)
    }

}

