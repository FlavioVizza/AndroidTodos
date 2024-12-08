package com.flaviovizza.androidtodos.common

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.flaviovizza.androidtodos.R
import java.lang.ref.WeakReference

/**
 * Utility object for displaying and managing a loader dialog with an optional message.
 */
object DialogSpinner {

    /**
     * Holds a reference to the currently displayed alert dialog.
     * Used to ensure that only one loader dialog is shown at a time.
     */
    private var dialog: AlertDialog? = null

    /**
     * Weak reference to the TextView used to display the optional message in the loader dialog.
     * This helps prevent memory leaks by avoiding strong references to views.
     */
    private var textViewMessage: WeakReference<TextView>? = null

    /**
     * Displays a loader dialog with an optional message.
     *
     * @param context The context in which the dialog should be displayed.
     * @param message An optional message to display in the loader. Defaults to `null`.
     * @param isCancellable Indicates whether the dialog can be cancelled by the user. Defaults to `false`.
     */
    fun show(context: Context, message: String? = null, isCancellable: Boolean = false) {
        // Dismiss previous dialog
        dismiss()

        // Inflating il layout
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_spinner, null)

        // config dialog
        dialog = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(isCancellable) // Sets whether the dialog is cancellable
            .create()

        // dimension config
        dialog?.setOnShowListener {
            val window = dialog?.window
            window?.setLayout(
                (context.resources.displayMetrics.widthPixels * 0.5).toInt(), // 50% of the screen width
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // config message
        val messageView = view.findViewById<TextView>(R.id.textViewMessage)
        textViewMessage = WeakReference(messageView)
        message?.let { textViewMessage?.get()?.text = it }

        // Show
        dialog?.show()
    }

    /**
     * Dismisses the currently displayed loader dialog, if any.
     */
    fun dismiss() {
        if (dialog?.isShowing == true) { dialog?.dismiss() }
        dialog = null
        textViewMessage = null
    }
}

