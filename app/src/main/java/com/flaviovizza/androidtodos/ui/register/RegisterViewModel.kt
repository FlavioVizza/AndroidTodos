package com.flaviovizza.androidtodos.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flaviovizza.androidtodos.R
import com.flaviovizza.androidtodos.data.AuthApiRepository
import com.flaviovizza.androidtodos.data.LanguageRepository
import com.flaviovizza.androidtodos.data.model.LoginRequest
import com.flaviovizza.androidtodos.data.model.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the registration process.
 * It handles user input validation, interacts with the [AuthApiRepository] for registration,
 * and exposes LiveData to notify the UI about the registration result, errors, and loading state.
 *
 * @property email The LiveData representing the email input field in the registration form.
 * @property username The LiveData representing the username input field in the registration form.
 * @property password The LiveData representing the password input field in the registration form.
 * @property confirmPassword The LiveData representing the confirm password input field in the registration form.
 * @property registerResult The LiveData representing the result of the registration process (true for success, false for failure).
 * @property errorMessages The LiveData containing error messages (e.g., validation errors, API errors).
 * @property isLoading The LiveData indicating the loading state during registration.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authApiRepository: AuthApiRepository,
    private val language: LanguageRepository
) : ViewModel() {

    // LiveData form fields
    val email = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    // LiveData to notify registration result
    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> get() = _registerResult

    // LiveData to notify error
    private val _errorMessages = MutableLiveData<String>()
    val errorMessages: LiveData<String> get() = _errorMessages

    // LiveData to notify loading status
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    /**
     * Initiates the registration process by validating the input fields and sending a registration request
     * to the [AuthApiRepository] if the form is valid.
     * Updates the [registerResult] and [isLoading] LiveData based on the operation's outcome.
     */
    fun register() {
        val usernameValue = username.value ?: ""
        val emailValue = email.value ?: ""
        val passwordValue = password.value ?: ""
        val confirmPasswordValue = confirmPassword.value ?: ""

        if (!validateForm(usernameValue, emailValue, passwordValue, confirmPasswordValue)) return

        CoroutineScope(Dispatchers.Main).launch {
            _isLoading.value = true
            _registerResult.value = authApiRepository.register(RegisterRequest(usernameValue, emailValue, passwordValue))
            _isLoading.value = false
        }

    }

    /**
     * Validates the registration form inputs. Checks for empty fields, email format, and matching passwords.
     *
     * @param usernameValue The username entered by the user.
     * @param emailValue The email entered by the user.
     * @param passwordValue The password entered by the user.
     * @param confirmPasswordValue The password confirmation entered by the user.
     * @return True if the form is valid, false otherwise.
     */
    private fun validateForm(
        usernameValue: String,
        emailValue: String,
        passwordValue: String,
        confirmPasswordValue: String
    ): Boolean {
        if (usernameValue.isEmpty() || passwordValue.isEmpty() || emailValue.isEmpty() || confirmPasswordValue.isEmpty()) {
            _errorMessages.value = language.getString(R.string.register_all_fields_are_required)
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            _errorMessages.value = language.getString(R.string.register_invalid_email_format)
            return false
        }
        if (usernameValue.length < 2 || passwordValue.length < 8 ) {
            _errorMessages.value = language.getString(R.string.register_invalid_username_or_password)
            return false
        }
        if (passwordValue != confirmPasswordValue) {
            _errorMessages.value = language.getString(R.string.register_invalid_password_or_confirm_password)
            return false
        }

        return true
    }

}