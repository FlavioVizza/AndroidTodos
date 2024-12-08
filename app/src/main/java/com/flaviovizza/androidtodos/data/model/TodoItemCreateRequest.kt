package com.flaviovizza.androidtodos.data.model

/**
 * Represents a request to create a new to-do item.
 *
 * @property title The title of the new to-do item.
 * @property description A detailed description of the new to-do item.
 * @property completed Indicates whether the to-do item should be marked as completed upon creation.
 */
data class TodoItemCreateRequest(
    val title: String,
    val description: String,
    val completed: Boolean,
)