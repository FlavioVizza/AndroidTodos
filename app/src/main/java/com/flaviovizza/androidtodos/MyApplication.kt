package com.flaviovizza.androidtodos

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The [MyApplication] class is the base application class for the app.
 * It is annotated with [@HiltAndroidApp] to trigger Hilt's code generation and enable dependency injection
 * throughout the app's lifecycle.
 *
 * This class is used to initialize Dagger Hilt and provide access to the application's global dependencies
 * that can be injected into other components like Activities, Fragments, ViewModels, and more.
 *
 * @see HiltAndroidApp
 * */
@HiltAndroidApp
class MyApplication : Application(){
}