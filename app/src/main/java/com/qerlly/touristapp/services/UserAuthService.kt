package com.qerlly.touristapp.services

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.qerlly.touristapp.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthService @Inject constructor(
    private val firebase: FirebaseAuth,
) {
    val currentUser: Boolean
        get() = firebase.currentUser != null

    val userEmail: String?
        get() = firebase.currentUser?.email

    val userId: String?
        get() = firebase.currentUser?.uid

    @OptIn(ExperimentalCoroutinesApi::class)
    val authenticationStatus: Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            trySendBlocking(currentUser)
        }
        firebase.addAuthStateListener(listener)
        awaitClose { firebase.removeAuthStateListener(listener) }
    }
        .onStart { emit(currentUser) }
        .conflate()
        .distinctUntilChanged()

    suspend fun login(email: String, password: String): AuthResult {
        return firebase.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun register(email: String, password: String): AuthResult {
        return firebase.createUserWithEmailAndPassword(email, password).await()
    }

    fun forgetPassword(email: String) = firebase.sendPasswordResetEmail(email)

    fun changePassword(context: Context, email: String, password: String, newPassword: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
            firebase.currentUser?.reauthenticate(credential)
                ?.addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        firebase.currentUser!!.updatePassword(newPassword).addOnCompleteListener {
                            if (it.isSuccessful) Toast.makeText(context, R.string.success_pass, Toast.LENGTH_LONG).show()
                            else Toast.makeText(context, R.string.unknown_error, Toast.LENGTH_LONG).show()
                        }
                    } else Toast.makeText(context, R.string.passwords_old, Toast.LENGTH_LONG).show()
                }
    }

    fun logout() {
        firebase.signOut()
    }

    fun isUserGid(): Boolean = userEmail?.endsWith("@firma.gid.com") ?: false
}