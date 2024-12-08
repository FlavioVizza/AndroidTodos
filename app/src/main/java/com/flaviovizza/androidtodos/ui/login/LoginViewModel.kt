package com.flaviovizza.androidtodos.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flaviovizza.androidtodos.R
import com.flaviovizza.androidtodos.data.AppEventManager
import com.flaviovizza.androidtodos.data.AuthApiRepository
import com.flaviovizza.androidtodos.data.LanguageRepository
import com.flaviovizza.androidtodos.data.model.LoginRequest

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The [LoginViewModel] is responsible for managing the login process and related UI state.
 * It communicates with the [AuthApiRepository] to perform authentication, handles form validation,
 * and exposes LiveData for the login result, error messages, and loading state.
 * This ViewModel is injected using Hilt.
 *
 * @param authApiRepository The repository responsible for API calls related to authentication.
 * @param appEventManager The manager for handling application events like authentication state.
 * @param language The repository for fetching localized strings for error messages.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authApiRepository: AuthApiRepository,
    private val appEventManager: AppEventManager,
    private val language: LanguageRepository
) : ViewModel() {

    /**
     * LiveData for the username input field.
     * Holds the value of the entered username (email).
     */
    val username = MutableLiveData<String>()
    /**
     * LiveData for the password input field.
     * Holds the value of the entered password.
     */
    val password = MutableLiveData<String>()

    /**
     * LiveData for the login result.
     * Emits a Boolean value indicating whether the login attempt was successful or not.
     */
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    /**
     * LiveData for error messages.
     * Emits error messages to notify the user about invalid form inputs or failed login attempts.
     */
    private val _errorMessages = MutableLiveData<String>()
    val errorMessages: LiveData<String> get() = _errorMessages

    /**
     * LiveData to notify the UI about the loading state.
     * Emits a Boolean indicating whether a login request is in progress.
     */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    /**
     * Attempts to log in with the current username and password.
     * It validates the form inputs, performs the login request asynchronously,
     * and updates the LiveData accordingly.
     */
    fun login() {
        val usernameValue = username.value ?: ""
        val passwordValue = password.value ?: ""

        if (!validateForm(usernameValue, passwordValue)) return

        CoroutineScope(Dispatchers.Main).launch {
            _isLoading.value = true
            val loginResult = authApiRepository.login(LoginRequest(email = usernameValue, password = passwordValue))
            _loginResult.value = loginResult
            if (loginResult) appEventManager.notifyAuthState(true)
            _isLoading.value = false
        }
    }

    /**
     * Validates the form inputs (username and password).
     * Checks if the username is not empty, follows a valid email format,
     * and if the password is not empty.
     *
     * @param usernameValue The username entered by the user.
     * @param passwordValue The password entered by the user.
     * @return True if the form is valid, false otherwise.
     */
    private fun validateForm(usernameValue: String, passwordValue: String): Boolean{

        if (usernameValue.isEmpty() || passwordValue.isEmpty()) {
            _errorMessages.value = language.getString(R.string.login_empty_email_and_password)
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(usernameValue).matches()) {
            _errorMessages.value = language.getString(R.string.login_invalid_email_format)
            return false
        }

        return true
    }

}
