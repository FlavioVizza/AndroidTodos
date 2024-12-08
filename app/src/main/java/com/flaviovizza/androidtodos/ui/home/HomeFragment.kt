package com.flaviovizza.androidtodos.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.flaviovizza.androidtodos.R
import com.flaviovizza.androidtodos.data.model.TodoItem

import dagger.hilt.android.AndroidEntryPoint

import com.flaviovizza.androidtodos.databinding.FragmentHomeBinding

/**
 * Fragment representing the home screen of the app, where the list of to-do items is displayed.
 * This fragment manages the RecyclerView, handles user interactions, and observes changes
 * in the ViewModel state to update the UI accordingly.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    /**
     * ViewModel associated with this Fragment, injected via Hilt.
     */
    private val homeViewModel: HomeViewModel by viewModels()

    /**
     * View binding for the fragment layout.
     *
     * The backing field `_binding` is used to avoid memory leaks, and the [binding] property
     * ensures safe access.
     */
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    /**
     * Adapter for managing and displaying the to-do list items in the RecyclerView
     */
    private lateinit var adapter: TodoAdapter

    /**
     * Inflates the layout for this fragment and initializes the binding object.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called after the view is created. Sets up the RecyclerView, observers, and buttons.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupButtons()
    }

    /**
     * Sets up the FloatingActionButton to navigate to the detail page for adding a new to-do item.
     */
    private fun setupButtons(){
        binding.fab.setOnClickListener(this::goToDetailPage)
    }

    /**
     * Configures the RecyclerView by initializing the adapter and layout manager.
     * Observes the ViewModel's state to update the list content dynamically.
     */
    private fun setupRecyclerView() {
        // Setup RecyclerView and Adapter
        adapter = TodoAdapter (
            onItemClick = { todoItem -> navigateToDetails(todoItem) },
            emptyListMessage = getString(R.string.home_empty_list)
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe ViewModel State
        homeViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is TodoListState.Loading -> adapter.setLoading(true)
                is TodoListState.Content -> adapter.setItems(state.items)
                is TodoListState.Empty -> adapter.setItems(emptyList())
            }
        }
    }

    /**
     * Observes LiveData objects in the ViewModel and sets up swipe-to-refresh functionality.
     */
    private fun setupObservers() {
        // SwipeRefreshLayout
        homeViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing -> binding.swipeRefreshLayout.isRefreshing = isRefreshing }
        binding.swipeRefreshLayout.setOnRefreshListener { homeViewModel.refreshTodos() }

        homeViewModel.refreshTodoList.observe(viewLifecycleOwner){ if(it) homeViewModel.refreshTodos() }

        homeViewModel.errorMessages.observe(viewLifecycleOwner) { _ -> onError() }
    }

    /**
     * Navigates to the detail page for creating a new to-do item.
     */
    private fun goToDetailPage(view: View){
        findNavController().navigate(R.id.action_navigation_home_to_navigation_detaiil)
    }

    /**
     * Navigates to the detail page with the selected to-do item's ID.
     *
     * @param task The selected to-do item to pass to the detail page.
     */
    private fun navigateToDetails(task: TodoItem) {
        val bundle = Bundle().apply {
            putInt("taskId", task.todoId) // Add data to bundle
        }
        findNavController().navigate(R.id.action_navigation_home_to_navigation_detaiil, bundle)
    }

    /**
     * Displays a toast message when an unexpected error occurs.
     */
    private fun onError(){
        Toast.makeText(
            requireContext(),
            getString(R.string.common_unexpected_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Cleans up resources by nullifying the binding instance when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

