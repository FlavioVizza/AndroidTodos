package com.flaviovizza.androidtodos.ui.home

import android.animation.AnimatorInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.flaviovizza.androidtodos.R
import com.flaviovizza.androidtodos.data.model.TodoItem
import com.flaviovizza.androidtodos.databinding.ItemEmptyBinding
import com.flaviovizza.androidtodos.databinding.ItemTodoBinding

/**
 * Adapter for displaying a to-do list in a RecyclerView.
 * Supports three view types: loading skeletons, an empty message, and to-do items.
 *
 * @param onItemClick Callback invoked when a to-do item is clicked.
 * @param emptyListMessage The message to display when the list is empty.
 */
class TodoAdapter (
    private val onItemClick: (TodoItem) -> Unit,
    private val emptyListMessage: String
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * List of to-do items to display in the RecyclerView.
     * Initially set to an empty list.
     */
    private var todoItems: List<TodoItem> = emptyList()

    /**
     * Flag indicating whether the adapter is in a loading state.
     * When true, skeleton views are displayed instead of actual content.
     */
    private var isLoading: Boolean = false

    /**
     * Holds constants related to view types used in the RecyclerView adapter.
     * These constants help differentiate between item views, empty views, and loading views.
     */
    companion object {
        /**
         * Constant representing the view type for a to-do item.
         */
        private const val VIEW_TYPE_ITEM = 0

        /**
         * Constant representing the view type for the empty list message.
         */
        private const val VIEW_TYPE_EMPTY = 1

        /**
         * Constant representing the view type for loading skeleton views.
         */
        private const val VIEW_TYPE_LOADING = 2
    }

    /**
     * Determines the view type for the given position.
     */
    override fun getItemViewType(position: Int): Int {
        return when {
            isLoading -> VIEW_TYPE_LOADING
            todoItems.isEmpty() -> VIEW_TYPE_EMPTY
            else -> VIEW_TYPE_ITEM
        }
    }

    /**
     * Creates the appropriate ViewHolder for the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_skeleton, parent, false)
                SkeletonViewHolder(view)
            }
            VIEW_TYPE_EMPTY -> {
                val binding = ItemEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyViewHolder(binding)
            }
            else -> {
                val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TodoViewHolder(binding)
            }
        }
    }

    /**
     * Returns the number of items in the adapter.
     */
    override fun getItemCount(): Int {
        return when {
            isLoading -> 5 // Show 5 skeleton cards during loading
            todoItems.isEmpty() -> 1 // Show 1 empty message view
            else -> todoItems.size
        }
    }

    /**
     * Binds the data to the appropriate ViewHolder.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TodoViewHolder -> holder.bind(todoItems[position])
            is SkeletonViewHolder -> holder.bind() // Optional, can leave empty
            is EmptyViewHolder -> holder.bind()
        }
    }

    /**
     * Updates the adapter to show the loading state.
     * @param isLoading Whether to display loading skeletons.
     */
    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    /**
     * Updates the adapter with a new list of to-do items.
     * @param items The list of [TodoItem] to display.
     */
    fun setItems(items: List<TodoItem>) {
        this.isLoading = false
        this.todoItems = items
        notifyDataSetChanged()
    }

    /**
     * ViewHolder for displaying individual to-do items.
     */
    inner class TodoViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds a [TodoItem] to the view.
         * @param item The to-do item to display.
         */
        fun bind(item: TodoItem) {
            binding.todoText.text = item.title
            val iconRes = if (item.completed) R.drawable.ic_check_24px else R.drawable.ic_unchecked_24px
            val color = if (item.completed) R.color.primary_variant else R.color.error

            binding.todoIcon.setImageResource(iconRes)
            binding.todoIcon.setColorFilter(
                ContextCompat.getColor(binding.root.context, color),
                android.graphics.PorterDuff.Mode.SRC_IN
            )

            // Set click listener
            binding.root.setOnClickListener {
                onItemClick(item) // Notify the listener
            }
        }
    }

    /**
     * ViewHolder for displaying loading skeletons.
     */
    inner class SkeletonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val iconSkeleton: View = view.findViewById(R.id.skeletonIcon)
        private val textSkeleton: View = view.findViewById(R.id.skeletonText)

        /**
         * Binds the skeleton animation to the view.
         */
        fun bind() {
            val animation = AnimatorInflater.loadAnimator(itemView.context, R.animator.skeleton_animation)
            animation.setTarget(iconSkeleton)
            animation.start()

            val animationText = AnimatorInflater.loadAnimator(itemView.context, R.animator.skeleton_animation)
            animationText.setTarget(textSkeleton)
            animationText.start()
        }
    }

    /**
     * ViewHolder for displaying the empty state message.
     */
    inner class EmptyViewHolder(private val binding: ItemEmptyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the empty list message to the view.
         */
        fun bind() {
            binding.emptyText.text = emptyListMessage
        }
    }
}
