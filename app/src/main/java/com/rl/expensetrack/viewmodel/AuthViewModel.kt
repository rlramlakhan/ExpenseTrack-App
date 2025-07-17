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

    fun getUser(): FirebaseUser? = authRepository.getCurrentUser()
}

class AuthViewModelFactory(private val authRepository: AuthRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(authRepository) as T
    }
}