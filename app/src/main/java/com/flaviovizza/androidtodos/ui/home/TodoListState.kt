package com.flaviovizza.androidtodos.ui.home

import com.flaviovizza.androidtodos.data.model.TodoItem

/**
 * Represents the possible states of the to-do list in the UI.
 */
sealed class TodoListState {

    /**
     * Indicates that the to-do list is currently being loaded.
     */
    data object Loading : TodoListState()

    /**
     * Indicates that the to-do list is empty.
     */
    data object Empty : TodoListState()

    /**
     * Indicates that the to-do list contains items.
     * @param items The list of [TodoItem] objects to display.
     */
    data class Content(val items: List<TodoItem>) : TodoListState()

}