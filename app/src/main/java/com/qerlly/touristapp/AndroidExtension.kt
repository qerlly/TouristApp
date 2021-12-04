package com.qerlly.touristapp

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

val Context.userSettings: DataStore<Preferences> by preferencesDataStore(name = "userSettings")

fun Context.getActivity(): AppCompatActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is AppCompatActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

@Suppress("ThrowableNotThrown")
@OptIn(ExperimentalCoroutinesApi::class)
fun CollectionReference.asFlow(): Flow<QuerySnapshot> = callbackFlow {

    val registration = addSnapshotListener { value, error ->
        when {
            error != null -> {
                cancel(CancellationException("$this error", error))
            }
            value == null -> {
                cancel(CancellationException("$this null"))
            }
            else -> {
                trySendBlocking(value)
            }
        }
    }

    awaitClose {
        registration.remove()
    }
}

@Suppress("ThrowableNotThrown")
@OptIn(ExperimentalCoroutinesApi::class)
fun DocumentReference.asFlow(): Flow<DocumentSnapshot> = callbackFlow {

    val registration = addSnapshotListener { value, error ->
        when {
            error != null -> {
                cancel(CancellationException("$this error", error))
            }
            value == null -> {
                cancel(CancellationException("$this null"))
            }
            else -> {
                trySendBlocking(value)
            }
        }
    }

    awaitClose {
        registration.remove()
    }
}