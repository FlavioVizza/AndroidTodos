package com.flaviovizza.androidtodos

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.flaviovizza.androidtodos.data.StorageRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

/**
 * The main entry point for the application's UI. This activity handles navigation, theming, and language configuration.
 * It serves as the host for fragments and integrates with Jetpack Navigation, Hilt, and ViewModel.
 *
 * Annotated with [@AndroidEntryPoint] to enable dependency injection via Hilt.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    /**
     * ViewModel for managing app-wide business logic, injected via Hilt.
     */
    private val viewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration

    /**
     * Attaches a base context with the user's preferred language configuration.
     *
     * @param newBase The base context for the activity.
     */
    override fun attachBaseContext(newBase: Context?) {
        val configWithLanguage = configLanguage(newBase!!)
        val newContext = newBase.createConfigurationContext(configWithLanguage)
        super.attachBaseContext(newContext)
    }

    /**
     * Called when the activity is first created. Initializes navigation, theming, and observers.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeApp(savedInstanceState)
    }

    /**
     * Saves the current activity state to a [Bundle].
     *
     * @param outState A Bundle in which to save the state.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        // save current activity state in a bundle and use it in next onCreate function
    }

    /**
     * Initializes the app's core components such as navigation and theme configuration.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    private fun initializeApp(savedInstanceState: Bundle?) {
        bottomNavigationView = findViewById(R.id.nav_view)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        configBottomNavBar(savedInstanceState)
        configAppBar()
        configDestination()
        configTheme()
        configObservers()
    }

    /**
     * Configures the app's ActionBar to work with the navigation components.
     */
    private fun configAppBar() {
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_settings)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    /**
     * Configures the bottom navigation bar and sets up navigation graphs.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    @Suppress("UNUSED_PARAMETER")
    private fun configBottomNavBar(savedInstanceState: Bundle?) {
        setNavGraph(viewModel.isUserLoggedIn())
    }

    /**
     * Configures the app's language based on user preferences or system defaults.
     *
     * @param context The current context.
     * @return A [Configuration] object with the selected language applied.
     */
    private fun configLanguage(context: Context): Configuration {
        val defaultLang = Locale.getDefault().language
        Log.i(TAG, "System default language is: $defaultLang")

        val sharedPreferences = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        val selectedLanguage = sharedPreferences.getString(StorageRepository.StorageKeys.SELECTED_LANG, defaultLang)
        Log.i(TAG, "User selected language is: $selectedLanguage")

        val config = Configuration(context.resources.configuration)
        val locale = Locale(selectedLanguage ?: defaultLang)
        Locale.setDefault(locale)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return config
    }

    /**
     * Configures the app's theme based on user preferences or system settings.
     */
    private fun configTheme(){
        // System Theme
        val systemTheme = viewModel.getSystemTheme()
        Log.i(TAG, "System default theme is: $systemTheme")

        // App Settings Theme
        val appTheme = viewModel.getAppTheme()
        viewModel.setAppTheme(appTheme)
        Log.i(TAG, "App theme is: $appTheme")
    }

    /**
     * Sets up a listener to adjust UI components based on the current destination.
     */
    private fun configDestination() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    bottomNavigationView.visibility = View.GONE
                    supportActionBar?.hide()
                }
                R.id.registerFragment -> {
                    bottomNavigationView.visibility = View.GONE
                    supportActionBar?.show()
                }
                R.id.navigation_detaiil -> {
                    bottomNavigationView.visibility = View.VISIBLE
                    bottomNavigationView.menu.findItem(R.id.navigation_home).isChecked = true
                    supportActionBar?.show()
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                    supportActionBar?.show()
                }
            }
        }

    }

    /**
     * Observes changes in the authentication state and updates the navigation graph accordingly.
     */
    private fun configObservers(){
        viewModel.authState.observe(this){ isAuthenticated -> setNavGraph(isAuthenticated) }
    }

    /**
     * Updates the navigation graph based on the user's authentication state.
     *
     * @param isAuthenticated Indicates whether the user is authenticated.
     */
    private fun setNavGraph(isAuthenticated: Boolean) {
        val graph = if (isAuthenticated) {
            navController.navInflater.inflate(R.navigation.mobile_navigation)
        } else {
            navController.navInflater.inflate(R.navigation.mobile_auth)
        }
        navController.graph = graph
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home, null, NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.mobile_navigation, false).build())
                    true
                }
                R.id.navigation_settings -> {
                    navController.navigate(R.id.navigation_settings, null, NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.mobile_navigation, false).build())
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Handles navigation up actions.
     *
     * @return `true` if the action was successfully handled, `false` otherwise.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Handles the back button press behavior.
     *
     * Only closes the app when on the login page.
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Close app only in login page
        if (navController.currentDestination?.id == R.id.loginFragment) {
            super.onBackPressed()  // Default
        }
    }
}

