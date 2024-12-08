package com.flaviovizza.androidtodos.common

import android.content.Context
import androidx.appcompat.app.AlertDialog

/**
 * Utility object for displaying alert dialogs in a simplified way.
 */
object DialogAlert {

    /**
     * Displays an alert dialog with customizable title, message, and actions.
     *
     * @param context The context in which the dialog should be displayed.
     * @param title The title of the dialog.
     * @param message The message to be displayed in the dialog.
     * @param positiveButtonText The text for the positive button.
     * @param positiveAction A lambda function to execute when the positive button is clicked.
     * @param negativeButtonText The text for the negative button.
     * @param negativeAction A lambda function to execute when the negative button is clicked.
     */
    fun show(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String,
        positiveAction: () -> Unit,
        negativeButtonText: String,
        negativeAction: () -> Unit
    ) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { dialog, _ ->
                positiveAction()
                dialog.dismiss()
            }
            .setNegativeButton(negativeButtonText) { dialog, _ ->
                negativeAction()
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

}
