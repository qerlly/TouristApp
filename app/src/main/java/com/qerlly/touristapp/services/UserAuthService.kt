package com.qerlly.touristapp.services

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
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

    fun logout() {
        firebase.signOut()
    }
}