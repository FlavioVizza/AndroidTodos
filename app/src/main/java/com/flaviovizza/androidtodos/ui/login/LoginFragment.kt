package com.flaviovizza.androidtodos.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.flaviovizza.androidtodos.R
import com.flaviovizza.androidtodos.common.DialogSpinner
import com.flaviovizza.androidtodos.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * The [LoginFragment] is responsible for displaying the login screen and handling user interactions.
 * It binds the UI components to the [LoginViewModel] and observes LiveData to reflect changes in the UI state.
 * It also manages navigation between the login and registration screens.
 *
 * This fragment is injected using Dagger Hilt and communicates with the [LoginViewModel] for login operations.
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {

    /**
     * The [LoginViewModel] instance, which handles the business logic of logging in and managing UI state.
     * This ViewModel is injected by Dagger Hilt.
     */
    private val viewModel: LoginViewModel by viewModels()

    /**
     * The [NavController] is used for handling navigation between fragments.
     */
    private lateinit var navController: NavController

    /**
     * The [Button] to navigate to the registration screen.
     */
    private lateinit var registerBtn: Button

    /**
     * Inflates the layout for this fragment and sets up the [NavController] and [registerBtn].
     *
     * @param inflater The LayoutInflater object to inflate the views.
     * @param container The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState The saved state of the fragment if any.
     * @return The inflated view for this fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        navController = findNavController()
        registerBtn = view.findViewById(R.id.register_button)
        return view
    }

    /**
     * Binds the UI components with the [LoginViewModel] and sets up the necessary observers for login state.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState The saved state of the fragment if any.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerBtn.setOnClickListener(this::goToRegisterPage)

        val binding: FragmentLoginBinding = FragmentLoginBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.errorMessages.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { success ->
            if (success) onLoginSuccess()
            else onLoginFail()
        }

       viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
           if (isLoading) DialogSpinner.show(requireContext())
           else DialogSpinner.dismiss()
       }

    }

    /**
     * Navigates to the registration screen when the register button is clicked.
     *
     * @param view The view that was clicked.
     */
    private fun goToRegisterPage(view: View) {
        navController.navigate(R.id.action_loginFragment_to_navigation_register)
    }

    /**
     * Handles the successful login scenario by showing a success message to the user.
     */
    private fun onLoginSuccess(){
        Toast.makeText(requireContext(), getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
    }

    /**
     * Handles the failed login scenario by showing a failure message to the user.
     */
    private fun onLoginFail(){
        Toast.makeText(requireContext(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
    }

}
