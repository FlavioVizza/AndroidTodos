package com.flaviovizza.androidtodos.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flaviovizza.androidtodos.data.AppEventManager
import com.flaviovizza.androidtodos.data.TodoApiRepository
import com.flaviovizza.androidtodos.data.model.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for managing the logic of the HomeFragment.
 *
 * Responsible for loading and managing the to-do list, handling refresh events,
 * and reporting states and errors to the UI.
 * Uses Hilt for dependency injection.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appEventManager: AppEventManager,
    private val todosRepo: TodoApiRepository
) : ViewModel() {

    /**
     * LiveData representing the current state of the to-do list.
     * Can be [TodoListState.Loading], [TodoListState.Empty], or [TodoListState.Content].
     */
    private val _state = MutableLiveData<TodoListState>()
    val state: LiveData<TodoListState> get() = _state

    /**
     * LiveData indicating whether a refresh operation is in progress.
     */
    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    /**
     * LiveData for reporting error messages to the UI.
     */
    private val _errorMessages = MutableLiveData<String>()
    val errorMessages: LiveData<String> get() = _errorMessages

    /**
     * LiveData triggered by external events to refresh the to-do list.
     * Observes changes from [appEventManager.refreshTodoList].
     */
    val refreshTodoList = appEventManager.refreshTodoList

    /**
     * Initializes the ViewModel and loads the initial to-do list.
     */
    init { loadTodos() }

    /**
     * Refreshes the to-do list by calling [loadTodos].
     * Typically invoked by pull-to-refresh or external refresh triggers.
     */
    fun refreshTodos() { loadTodos() }

    /**
     * Loads the to-do list from the repository and updates the UI state.
     *
     * Sets [TodoListState.Loading] initially, then updates the state with the result:
     * - [TodoListState.Content] if there are items.
     * - [TodoListState.Empty] if the list is empty.
     *
     * In case of errors, updates [errorMessages] for UI feedback.
     */
    private fun loadTodos() {
        _state.value = TodoListState.Loading
        _isRefreshing.value = true

        viewModelScope.launch {
            try {
                val todos = todosRepo.getTodos() ?: emptyList()
                _state.value = if (todos.isEmpty())  TodoListState.Empty else TodoListState.Content(todos)
            }
            catch (e: Exception) { _errorMessages.value = e.message }
            finally { _isRefreshing.value = false }
        }
    }

}



