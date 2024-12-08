package com.flaviovizza.androidtodos.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A singleton class responsible for managing application events, such as user authentication state and to-do list refresh notifications.
 * This class uses Hilt for dependency injection.
 */
@Singleton
class AppEventManager @Inject constructor(
    private val storageRepo: StorageRepository
) {

    /**
     * Checks whether the user is logged in by verifying if an access token is present.
     *
     * @return `true` if the user is logged in (access token is available), otherwise `false`.
     */
    fun isUserLoggedIn(): Boolean {
        val jwt = storageRepo.getData(StorageRepository.StorageKeys.ACCESS_TOKEN)
        return jwt?.isNotEmpty() ?: false
    }

    /**
     * LiveData representing the authentication state of the user.
     * Observers can use this to track changes in authentication state.
     */
    private val _authState = MutableLiveData<Boolean>()
    val authState: LiveData<Boolean> = _authState
    /**
     * Notifies observers of a change in the authentication state.
     * Clears stored data such as the access and refresh tokens when [authState] is false
     *
     * @param authState The new authentication state, where `true` indicates the user is authenticated and `false` indicates the user is not.
     */
    fun notifyAuthState(authState: Boolean){
        if(!authState){
            storageRepo.deleteData(StorageRepository.StorageKeys.ACCESS_TOKEN)
            storageRepo.deleteData(StorageRepository.StorageKeys.REFRESH_TOKEN)
        }
        _authState.postValue(authState)
    }

    /**
     * LiveData representing the need to refresh the to-do list.
     * Observers can use this to trigger a refresh of the to-do list when changes occur.
     */
    private val _refreshTodoList = MutableLiveData<Boolean>()
    val refreshTodoList: LiveData<Boolean> = _refreshTodoList
    /**
     * Notifies observers that the to-do list has changed and needs to be refreshed.
     *
     * @param isChanged A flag indicating whether the to-do list has changed.
     */
    fun notifyTodoListChanged(isChanged: Boolean){
        if(isChanged) _refreshTodoList.value = true
    }

}