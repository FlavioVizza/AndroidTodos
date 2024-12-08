package com.flaviovizza.androidtodos.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flaviovizza.androidtodos.data.AppEventManager
import com.flaviovizza.androidtodos.data.TodoApiRepository
import com.flaviovizza.androidtodos.data.model.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    private val appEventManager: AppEventManager,
    private val todosRepo: TodoApiRepository
) : ViewModel() {

    /**
     * LiveData properties for task attributes.
     */
    val taskId = MutableLiveData(-1)
    val title = MutableLiveData("")
    val description = MutableLiveData("")
    val createAt = MutableLiveData("")
    val isDone = MutableLiveData(false)

    /**
     * Mode of the fragment, either "create" for new tasks or "update" for editing existing ones.
     */
    val mode = MutableLiveData("create") // Can be "create" or "update"

    /**
     * LiveData to track the loading state of the ViewModel.
     */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    /**
     * LiveData to report error messages for UI feedback.
     */
    private val _errorMessages = MutableLiveData<String>()
    val errorMessages: LiveData<String> get() = _errorMessages


    /**
     * Loads the details of an existing to-do task for editing.
     *
     * @param id The ID of the task to load.
     */
    fun loadTodo(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val todo = todosRepo.getTodoById(id)!!
            taskId.value = todo.todoId
            title.value = todo.title
            description.value = todo.description
            createAt.value = todo.createAt
            isDone.value = todo.completed
            _isLoading.value = false
            mode.value = "update"
        }
    }

    /**
     * Creates a new to-do task.
     *
     * @return `true` if the task is successfully created, otherwise `false`.
     */
    suspend fun createTask(): Boolean {
        _isLoading.value = true
        return try {
            val result = todosRepo.createTodoItem(title.value!!, description.value!!, isDone.value!!)
            appEventManager.notifyTodoListChanged(result?.success ?: false)
            result?.success ?: false
        } catch (e: Exception) {
            _errorMessages.value = e.message
            false
        } finally { _isLoading.value = false  }
    }

    /**
     * Updates an existing to-do task.
     *
     * @return `true` if the task is successfully updated, otherwise `false`.
     */
    suspend fun updateTask() : Boolean {
        _isLoading.value = true
        return try {
            val todoItem = TodoItem(taskId.value!!, title.value!!, description.value!!, isDone.value!!, createAt.value!!)
            val result = todosRepo.updateTodoById(todoItem)
            appEventManager.notifyTodoListChanged(result?.success ?: false)
            _isLoading.value = false
            result?.success ?: false
        } catch (e: Exception) {
            _errorMessages.value = e.message
            false
        } finally { _isLoading.value = false  }
    }

    /**
     * Deletes the current to-do task.
     *
     * @return `true` if the task is successfully deleted, otherwise `false`.
     */
    suspend fun deleteTask() : Boolean {
        _isLoading.value = true
        return try {
            val result = todosRepo.deleteTodoById(taskId.value!!)
            appEventManager.notifyTodoListChanged(result?.success ?: false)
            _isLoading.value = false
            result?.success ?: false
        } catch (e: Exception) {
            _errorMessages.value = e.message
            false
        } finally { _isLoading.value = false  }
    }

}