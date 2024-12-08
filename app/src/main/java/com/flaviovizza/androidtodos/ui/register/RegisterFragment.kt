package com.flaviovizza.androidtodos.ui.register

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.flaviovizza.androidtodos.R
import com.flaviovizza.androidtodos.common.DialogSpinner
import com.flaviovizza.androidtodos.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment responsible for handling the user registration UI.
 * It binds the [RegisterViewModel] to the UI, listens for user input,
 * and observes the registration result, error messages, and loading state.
 *
 * @property viewModel The [RegisterViewModel] that handles the registration logic and manages the UI state.
 * @property navController The [NavController] used for navigation between fragments.
 */
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    /**
     * The ViewModel that provides data and handles logic related to user registration.
     */
    private val viewModel: RegisterViewModel by viewModels()

    /**
     * The NavController responsible for handling fragment navigation.
     */
    private lateinit var navController: NavController

    /**
     * Inflates the fragment's layout and initializes the NavController.
     *
     * @param inflater The [LayoutInflater] object to inflate the fragment's layout.
     * @param container The parent view group that holds the fragment's view hierarchy.
     * @param savedInstanceState The saved state of the fragment, if any.
     *
     * @return The root view of the fragment, inflated with the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        navController = findNavController()
        return view
    }

    /**
     * Called after the fragment's view has been created. This method binds the ViewModel to the UI,
     * sets up observers for LiveData, and handles UI updates based on the registration state.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState The saved instance state of the fragment, if any.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding: FragmentRegisterBinding = FragmentRegisterBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.errorMessages.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        viewModel.registerResult.observe(viewLifecycleOwner) { success ->
            if (success) onRegisterSuccess()
            else onRegisterFail()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) DialogSpinner.show(requireContext())
            else DialogSpinner.dismiss()
        }
    }

    /**
     * Handles the success case when registration is successful.
     * Shows a success message and navigates back to the previous screen.
     */
    private fun onRegisterSuccess(){
        Toast.makeText(requireContext(), getString(R.string.register_success), Toast.LENGTH_SHORT).show()
        navController.popBackStack()
    }

    /**
     * Handles the failure case when registration fails.
     * Shows a failure message to the user.
     */
    private fun onRegisterFail(){
        Toast.makeText(requireContext(), getString(R.string.register_failed), Toast.LENGTH_SHORT).show()
    }

}