package com.flaviovizza.androidtodos.network

import com.flaviovizza.androidtodos.data.model.TodoItemCreateRequest
import com.flaviovizza.androidtodos.data.model.GenericResponse
import com.flaviovizza.androidtodos.data.model.TodoItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

/**
 * API service interface for managing to-do items.
 * Provides methods to perform CRUD operations on to-do items, including fetching, creating, updating, and deleting.
 */
interface TodoApiService {

    /**
     * Fetches the list of all to-do items from the API.
     *
     * @return A `Response` containing a list of `TodoItem` objects if the request is successful.
     */
    @GET("todos")
    suspend fun getTodos(): Response<List<TodoItem>>

    /**
     * Creates a new to-do item with the provided data.
     *
     * @param request The request containing the new to-do item details (title, description, and completion status).
     * @return A `Response` containing a `GenericResponse` that indicates the result of the creation operation.
     */
    @POST("todos")
    suspend fun createTodoItem(@Body request: TodoItemCreateRequest): Response<GenericResponse>

    /**
     * Fetches a to-do item by its ID from the API.
     *
     * @param todoId The ID of the to-do item to fetch.
     * @return A `Response` containing the `TodoItem` object if the request is successful.
     */
    @GET("todos/{todoId}")
    suspend fun getTodoById(@Path("todoId") todoId: Int): Response<TodoItem>

    /**
     * Updates the to-do item with the specified ID with new data.
     *
     * @param todoId The ID of the to-do item to update.
     * @param todoItem The updated `TodoItem` object containing the new data.
     * @return A `Response` containing a `GenericResponse` that indicates the result of the update operation.
     */
    @PUT("todos/{todoId}")
    suspend fun updateTodoById(@Path("todoId") todoId: Int, @Body todoItem: TodoItem): Response<GenericResponse>

    /**
     * Deletes the to-do item with the specified ID.
     *
     * @param todoId The ID of the to-do item to delete.
     * @return A `Response` containing a `GenericResponse` that indicates the result of the deletion operation.
     */
    @DELETE("todos/{todoId}")
    suspend fun deleteTodoById(@Path("todoId") todoId: Int): Response<GenericResponse>

}