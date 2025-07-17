package com.rl.expensetrack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.rl.expensetrack.model.repositories.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {

    private val _authResult = MutableLiveData<Result<FirebaseUser?>>()
    val authResult: LiveData<Result<FirebaseUser?>> = _authResult

    private val _signOutResult = MutableLiveData<Result<Boolean>>()
    val signOutResult: LiveData<Result<Boolean>> = _signOutResult

    private val _resetLinkResult = MutableLiveData<Result<Boolean>>()
    val resetLinkResult: LiveData<Result<Boolean>> get() = _resetLinkResult

    private val _userStatus = MutableLiveData<Boolean>()
    val userStatus: LiveData<Boolean> get() = _userStatus

    fun signIn(email: String, password: String) {
        authRepository.signIn(email, password).observeForever {
            _authResult.value = it
        }
    }

    fun signUp(email: String, password: String) {
        authRepository.signUp(email, password).observeForever {
            _authResult.value = it
        }
    }

    fun signOut() {
        authRepository.signOut().observeForever {
            _signOutResult.value = it
        }
    }

    fun resetPassword(email: String) {
        authRepository.resetPassword(email).observeForever {
            _resetLinkResult.value = it
        }
    }

    fun getUser() {
        val currentUser = authRepository.getCurrentUser()
        _userStatus.value = currentUser != null
    }

}

class AuthViewModelFactory(private val authRepository: AuthRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(authRepository) as T
    }
}