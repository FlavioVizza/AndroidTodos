package com.flaviovizza.androidtodos.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.flaviovizza.androidtodos.R
import com.flaviovizza.androidtodos.common.DialogAlert
import com.flaviovizza.androidtodos.ui.language.LanguageListDialogFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment that handles user settings such as theme selection, language change, and logout.
 * This fragment allows users to toggle the theme, change the language, and log out of the application.
 *
 * @property viewModel The [SettingsViewModel] that manages the data and business logic for the settings screen.
 * @property themeSwitch The [Switch] for toggling the dark theme.
 * @property btnLanguage The [ImageButton] for opening the language selection dialog.
 * @property btnLogout The [ImageButton] for logging out the user.
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var themeSwitch: Switch
    private lateinit var btnLanguage: ImageButton
    private lateinit var btnLogout: ImageButton

    /**
     * Inflates the settings fragment's layout and initializes the UI components (theme switch, language button, and logout button).
     *
     * @param inflater The [LayoutInflater] object to inflate the fragment's layout.
     * @param container The parent view group that holds the fragment's view hierarchy.
     * @param savedInstanceState The saved instance state of the fragment, if any.
     *
     * @return The root view of the fragment, inflated with the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Dark Theme Switch
        themeSwitch = view.findViewById(R.id.theme_switch)
        themeSwitch.isChecked = viewModel.isDarkTheme(viewModel.getActualTheme())
        themeSwitch.setOnCheckedChangeListener(this::onThemeSwitchButtonView)

        // Language Button
        btnLanguage = view.findViewById(R.id.btnLanguage)
        btnLanguage.setOnClickListener(this::changeLanguage)

        // Logout Button
        btnLogout = view.findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener(this::logout)

        return view
    }

    /**
     * Handles the theme switch toggle event. Updates the theme based on the switch's state.
     *
     * @param switchBtn The [CompoundButton] representing the theme switch.
     * @param isChecked The current state of the switch, indicating whether dark theme is enabled.
     */
    @Suppress("UNUSED_PARAMETER")
    private fun onThemeSwitchButtonView(switchBtn: CompoundButton, isChecked: Boolean) {
        Log.i(javaClass.name, "onThemeSwitchButtonView: $isChecked")
        this.viewModel.setAppTheme(isChecked)
        findNavController().navigate(R.id.navigation_home)
    }

    /**
     * Opens the language selection dialog where users can select a language.
     *
     * @param view The [View] that triggered the language change event.
     */
    @Suppress("UNUSED_PARAMETER")
    private fun changeLanguage(view: View){
        val items = viewModel.getAppLanguages() //listOf(R.string.en, R.string.it)
        val dialog = LanguageListDialogFragment.newInstance(items, true)
        dialog.setCallback { selectedItem -> applyLanguage(selectedItem) }
        dialog.show(parentFragmentManager, "LanguageListDialogFragment")
    }

    /**
     * Applies the selected language and prompts the user for confirmation before changing the language.
     *
     * @param selectedResourceId The resource ID of the selected language.
     */
    private fun applyLanguage(selectedResourceId: Int){
        val lang = viewModel.getLanguageCodeBy(selectedResourceId)
        DialogAlert.show(
            context = requireContext(),
            title = getString(R.string.settings_change_language_alert_title),
            message = getString(R.string.settings_change_language_alert_body),
            positiveButtonText = getString(R.string.common_yes),
            positiveAction = {
                viewModel.changeLanguage(lang)
                requireActivity().recreate()
            },
            negativeButtonText = getString(R.string.common_no),
            negativeAction = {  }
        )
    }

    /**
     * Handles the logout process by showing a confirmation dialog before logging out.
     *
     * @param v The [View] that triggered the logout event.
     */
    @Suppress("UNUSED_PARAMETER")
    private fun logout( v : View) {
        DialogAlert.show(
            context = requireContext(),
            title = getString(R.string.settings_confirm_logout),
            message = getString(R.string.settings_confirm_logout_message),
            positiveButtonText = getString(R.string.common_yes),
            positiveAction = { viewModel.logout() },
            negativeButtonText = getString(R.string.common_no),
            negativeAction = { }
        )
    }

}
