package com.jessica.yourfavoritemovies.authentication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.jessica.yourfavoritemovies.MovieUtil

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<String> =  MutableLiveData()
    var stateRegister: MutableLiveData<Boolean> = MutableLiveData()
    var stateLogin: MutableLiveData<Boolean> = MutableLiveData()

    fun registerUser(email: String, password: String) {
        loading.value = true
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                loading.value = false
                when {
                    task.isSuccessful -> {
                        MovieUtil.setUserId(
                            getApplication(),
                            FirebaseAuth.getInstance().currentUser?.uid
                        )
                        stateRegister.value = true
                    }
                    else -> {
                        errorMessage("Register failed")
                        stateRegister.value = false
                    }
                }
        }
    }

    fun loginEmailPassword(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                loading.value = false
                when {
                    task.isSuccessful -> {
                        MovieUtil.setUserId(
                            getApplication(),
                            FirebaseAuth.getInstance().currentUser?.uid
                        )
                        stateLogin.value = true
                    }
                    else -> {
                        errorMessage("Login Failed")
                        stateLogin.value = false
                    }
                }
            }
    }

    private fun errorMessage(errorMessage: String) {
        error.value = errorMessage
    }

}