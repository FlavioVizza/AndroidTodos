package com.flaviovizza.androidtodos.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A repository class responsible for managing local storage using SharedPreferences.
 * This class provides methods to save, retrieve, and delete data from SharedPreferences.
 * It uses Hilt for dependency injection and is annotated as a Singleton.
 */
@Singleton
class StorageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Object holding constant keys for different types of data stored in SharedPreferences.
     * These keys are used to save or retrieve specific settings and authentication tokens.
     */
    object StorageKeys  {
        const val APP_THEME = "settings.apptheme"
        const val SELECTED_LANG = "settings.selectedlang"
        const val ACCESS_TOKEN = "auth.token.access"
        const val REFRESH_TOKEN = "auth.token.refresh"
    }

    /**
     * SharedPreferences instance to interact with app's private storage
     */
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "user_preferences",
        Context.MODE_PRIVATE
    )

    /**
     * Saves a key-value pair in SharedPreferences.
     *
     * @param key The key under which the value is stored.
     * @param value The value to store. It should be a string.
     */
    fun saveData(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    /**
     * Retrieves a value from SharedPreferences using the specified key.
     *
     * @param key The key to look up in SharedPreferences.
     * @return The value associated with the key, or `null` if the key does not exist.
     */
    fun getData(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    /**
     * Deletes a specific key-value pair from SharedPreferences.
     *
     * @param key The key to remove from SharedPreferences.
     */
    fun deleteData(key: String){
        sharedPreferences.edit().remove(key).apply()
    }
}