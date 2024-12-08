package com.flaviovizza.androidtodos.data.model

import com.flaviovizza.androidtodos.data.model.TodoItem

/**
 * Represents a list of to-do items.
 *
 * @property items The list of to-do items.
 */
data class TodoList(
    val items: List<TodoItem>
)
