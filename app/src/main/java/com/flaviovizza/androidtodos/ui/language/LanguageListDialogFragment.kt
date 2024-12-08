package com.flaviovizza.androidtodos.ui.language

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.flaviovizza.androidtodos.R
import com.flaviovizza.androidtodos.databinding.FragmentLanguageListItemBinding
import com.flaviovizza.androidtodos.databinding.FragmentLanguageListLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * A BottomSheetDialogFragment that displays a list of languages in a bottom sheet dialog.
 * This fragment allows the user to select a language from the list.
 * The selected item triggers a callback to notify the calling component.
 *
 * @property _binding The backing field for the view binding, ensuring safe access to the fragment's views.
 * @property binding The non-nullable access to the binding, used to interact with views safely.
 * @property list The RecyclerView that displays the list of language items.
 * @property items The list of language resource IDs to be displayed.
 * @property callback A lambda function that is invoked when an item in the list is selected.
 */
class LanguageListDialogFragment : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_ITEMS = "arg_items"
        private const val ARG_FULL_SCREEN = "arg_full_screen"

        /**
         * Creates a new instance of [LanguageListDialogFragment] with the specified items and fullscreen flag.
         *
         * @param items A list of language resource IDs to display in the dialog.
         * @param isFullScreen A flag indicating if the bottom sheet should be fullscreen.
         * @return A new instance of [LanguageListDialogFragment].
         */
        fun newInstance(items: List<Int>, isFullScreen: Boolean): LanguageListDialogFragment {
            return LanguageListDialogFragment().apply {
                arguments = Bundle().apply {
                    putIntegerArrayList(ARG_ITEMS, ArrayList(items))
                    putBoolean(ARG_FULL_SCREEN, isFullScreen)
                }
            }
        }
    }

    /**
     * View binding for the fragment layout.
     *
     * The backing field `_binding` is used to avoid memory leaks, and the [binding] property
     * ensures safe access.
     */
    private var _binding: FragmentLanguageListLayoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var list: RecyclerView
    private lateinit var items: List<Int>
    private var callback: ((Int) -> Unit)? = null
    fun setCallback(callback: (Int) -> Unit) { this.callback = callback }

    /**
     * Called to initialize the fragment. Sets the list of items from the arguments.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            items = it.getIntegerArrayList(ARG_ITEMS)?.toList() ?: emptyList()
        }
    }

    /**
     * Inflates the layout for the fragment and returns the root view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageListLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Sets up the RecyclerView and adapter, and binds the click action for each item.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list = binding.list
        list.layoutManager = LinearLayoutManager(context)

        list.adapter = ItemAdapter(items) { selectedItemResId ->
            callback?.invoke(selectedItemResId)
            dismiss()
        }
    }

    /**
     * Adjusts the bottom sheet behavior based on whether it should be fullscreen or not.
     */
    override fun onStart() {
        super.onStart()

        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheet?.let {
            val layoutParams = it.layoutParams
            val isFullScreen = arguments?.getBoolean(ARG_FULL_SCREEN) ?: false

            layoutParams.height = if (isFullScreen) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            it.layoutParams = layoutParams

            val behavior = BottomSheetBehavior.from(it)
            if (isFullScreen) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            } else {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
            }
        }
    }

    /**
     * Cleans up resources by nullifying the binding object when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * RecyclerView Adapter that binds language items to views in the list.
     */
    private inner class ItemAdapter(private val items: List<Int>, private val onItemClick: (Int) -> Unit)
        : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

        /**
         * ViewHolder to hold the views for each language item.
         */
        inner class ViewHolder(binding: FragmentLanguageListItemBinding) : RecyclerView.ViewHolder(binding.root) {
            val text: TextView = binding.text
        }

        /**
         * Returns the number of items in the list.
         */
        override fun getItemCount(): Int = items.size

        /**
         * Creates a ViewHolder for each item in the list.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                FragmentLanguageListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        /**
         * Binds the data to the ViewHolder.
         */
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val resourceId = items[position]
            holder.text.text = getString(resourceId)
            holder.itemView.setOnClickListener { onItemClick(resourceId) }
        }

    }

}