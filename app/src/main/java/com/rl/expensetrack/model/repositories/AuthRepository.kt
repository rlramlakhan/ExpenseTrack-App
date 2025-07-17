package com.rl.expensetrack.model.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signIn(email: String, password: String): LiveData<Result<FirebaseUser?>> {
        val result = MutableLiveData<Result<FirebaseUser?>>()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.value = Result.success(firebaseAuth.currentUser)
                } else {
                    result.value = Result.failure(task.exception ?: Exception(task.exception?.message))
                }
            }
        return result
    }

    fun signUp(email: String, password: String): LiveData<Result<FirebaseUser?>> {
        val result = MutableLiveData<Result<FirebaseUser?>>()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.value = Result.success(firebaseAuth.currentUser)
                } else {
                    result.value = Result.failure(task.exception ?: Exception(task.exception?.message))
                }
            }
        return result
    }

    fun signOut(): LiveData<Result<Boolean>> {
        val result = MutableLiveData<Result<Boolean>>()

        return try {
            firebaseAuth.signOut()
            result.value = Result.success(true)
            result
        } catch (e: Exception) {
            result.value = Result.failure(e)
            result
        }
    }

    fun resetPassword(email: String): LiveData<Result<Boolean>> {
        val result = MutableLiveData<Result<Boolean>>()
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.value = Result.success(true)
                } else {
                    result.value = Result.failure(task.exception ?: Exception(task.exception?.message))
                }
            }
        return result
    }
    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser
}