package com.flaviovizza.androidtodos.data

import com.flaviovizza.androidtodos.data.model.TodoItemCreateRequest
import com.flaviovizza.androidtodos.data.model.GenericResponse
import com.flaviovizza.androidtodos.data.model.TodoItem
import com.flaviovizza.androidtodos.network.TodoApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A repository class responsible for interacting with the to-do API service.
 * It provides methods to perform CRUD operations on To-do items, including fetching, creating, updating, and deleting.
 * This class uses Hilt for dependency injection and is annotated as a Singleton.
 */
@Singleton
class TodoApiRepository@Inject constructor(
    private val todoApiService: TodoApiService
) {

    /**
     * Fetches the list of all to-do items from the API.
     *
     * @return A list of `TodoItem` objects if the request is successful, or `null` if the request fails.
     * @throws Exception If the response is not successful, an exception is thrown with the error code and message.
     */
    suspend fun getTodos(): List<TodoItem>?{
        return withContext(Dispatchers.IO) {
            val response = todoApiService.getTodos()
            if (response.isSuccessful) response.body()
            else throw Exception("Error: ${response.code()} - ${response.message()}")
        }
    }

    /**
     * Creates a new tod-o item by sending the provided details to the API.
     *
     * @param title The title of the new to-do item.
     * @param description The description of the new to-do item.
     * @param done A boolean indicating whether the to-do item is completed.
     * @return A `GenericResponse` containing the result of the operation, or `null` if the request fails.
     * @throws Exception If the response is not successful, an exception is thrown with the error code and message.
     */
    suspend fun createTodoItem(title: String, description: String, done: Boolean): GenericResponse? {
        val todoItem = TodoItemCreateRequest(title = title, description = description, completed = done)
        return withContext(Dispatchers.IO) {
            val response = todoApiService.createTodoItem(todoItem)
            if (response.isSuccessful) response.body()
            else throw Exception("Error: ${response.code()} - ${response.message()}")
        }
    }

    /**
     * Fetches a to-do item by its ID from the API.
     *
     * @param todoId The ID of the to-do item to fetch.
     * @return The `TodoItem` object if the request is successful, or `null` if the request fails.
     * @throws Exception If the response is not successful, an exception is thrown with the error code and message.
     */
    suspend fun getTodoById(todoId: Int): TodoItem? {
        return withContext(Dispatchers.IO) {
            val response = todoApiService.getTodoById(todoId)
            if (response.isSuccessful) response.body()
            else throw Exception("Error: ${response.code()} - ${response.message()}")
        }
    }

    /**
     * Updates a to-do item by its ID with the new data provided.
     *
     * @param todoItem The `TodoItem` object containing the updated data to be sent to the API.
     * @return A `GenericResponse` containing the result of the update operation, or `null` if the request fails.
     * @throws Exception If the response is not successful, an exception is thrown with the error code and message.
     */
    suspend fun updateTodoById(todoItem: TodoItem): GenericResponse?{
        return withContext(Dispatchers.IO) {
            val response = todoApiService.updateTodoById(todoItem.todoId, todoItem)
            if (response.isSuccessful) response.body()
            else throw Exception("Error: ${response.code()} - ${response.message()}")
        }
    }

    /**
     * Deletes a to-do item by its ID.
     *
     * @param todoId The ID of the to-do item to delete.
     * @return A `GenericResponse` containing the result of the deletion operation, or `null` if the request fails.
     * @throws Exception If the response is not successful, an exception is thrown with the error code and message.
     */
    suspend fun deleteTodoById(todoId: Int): GenericResponse?{
        return withContext(Dispatchers.IO) {
            val response =  todoApiService.deleteTodoById(todoId)
            if (response.isSuccessful) response.body()
            else throw Exception("Error: ${response.code()} - ${response.message()}")
        }
    }

}