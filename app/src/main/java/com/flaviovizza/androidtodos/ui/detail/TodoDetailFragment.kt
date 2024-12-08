package com.flaviovizza.androidtodos.ui.detail

import androidx.navigation.fragment.findNavController
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.flaviovizza.androidtodos.R
import com.flaviovizza.androidtodos.common.DialogSpinner
import com.flaviovizza.androidtodos.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

/**
 * Fragment for displaying and managing the details of a to-do task.
 *
 * Annotated with [@AndroidEntryPoint] to support dependency injection via Hilt.
 */
@AndroidEntryPoint
class TodoDetailFragment : Fragment() {

    /**
     * ViewModel associated with this Fragment, injected via Hilt.
     */
    private val viewModel: TodoDetailViewModel by viewModels()

    /**
     * View binding for the fragment layout.
     *
     * The backing field `_binding` is used to avoid memory leaks, and the [binding] property
     * ensures safe access.
     */
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    /**
     * Inflates the fragment's view and initializes the data binding.
     *
     * @param inflater The [LayoutInflater] object for inflating views.
     * @param container The parent container where the fragment's UI will be attached.
     * @param savedInstanceState The saved instance state of the fragment.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    /**
     * Called after the fragment's view has been created.
     *
     * Sets up navigation arguments, observers, and UI event listeners.
     *
     * @param view The fragment's root view.
     * @param savedInstanceState The saved instance state of the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavArgs()
        setupObservers()
        setupListeners()
    }

    /**
     * Retrieves navigation arguments passed to the fragment.
     *
     * Extracts the task ID and triggers data loading if the task ID is valid.
     */
    private fun setupNavArgs(){
        val taskId = arguments?.getInt("taskId", -1) ?: -1
        if (taskId != -1) { viewModel.loadTodo(taskId) }
    }

    /**
     * Sets up observers to monitor ViewModel data and react to changes in UI state.
     */
    private fun setupObservers() {
        viewModel.mode.observe(viewLifecycleOwner) {
            if (it == "create") {
                binding.buttonCreateOrUpdate.text = getString(R.string.detail_create_button)
                binding.switchDone.visibility = View.GONE
                binding.buttonDelete.visibility = View.GONE
            } else {
                binding.buttonCreateOrUpdate.text = getString(R.string.detail_update_button)
                binding.switchDone.visibility = View.VISIBLE
                binding.buttonDelete.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) DialogSpinner.show(requireContext())
            else DialogSpinner.dismiss()
        }

        viewModel.errorMessages.observe(viewLifecycleOwner) { _ -> onError() }
    }

    /**
     * Configures click listeners for UI elements.
     */
    private fun setupListeners() {
        binding.buttonCreateOrUpdate.setOnClickListener {
            runBlocking {
                val result =  if(viewModel.mode.value == "create") viewModel.createTask() else viewModel.updateTask()
                if (result) findNavController().popBackStack()  else onError()
            }
        }

        binding.buttonDelete.setOnClickListener {
            runBlocking {
                val result = viewModel.deleteTask()
                if (result) findNavController().popBackStack() else onError()
            }
        }
    }

    /**
     * Displays a generic error message to the user.
     */
    private fun onError(){
        Toast.makeText(requireContext(), getString(R.string.common_unexpected_error), Toast.LENGTH_SHORT).show()
    }

    /**
     * Cleans up resources when the fragment's view is destroyed.
     *
     * Sets the view binding to `null` to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
