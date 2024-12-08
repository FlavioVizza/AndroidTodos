package com.flaviovizza.androidtodos.data.model

/**
 * Represents a request to update an existing to-do item.
 *
 * @property title The updated title of the to-do item.
 * @property description The updated description of the to-do item.
 */
data class TodoItemRequest(
    val title: String,
    val description: String
)
