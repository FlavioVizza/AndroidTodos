package com.flaviovizza.androidtodos.data.model

/**
 * Represents a to-do item.
 *
 * @property todoId The unique identifier for the to-do item.
 * @property title The title of the to-do item.
 * @property description A detailed description of the to-do item.
 * @property completed Indicates whether the to-do item has been completed.
 * @property createAt The date and time when the to-do item was created.
 */
data class TodoItem(
    val todoId: Int,
    val title: String,
    val description: String,
    val completed: Boolean,
    val createAt: String
)
