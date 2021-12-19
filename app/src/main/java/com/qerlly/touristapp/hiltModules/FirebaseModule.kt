package com.qerlly.touristapp.hiltModules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideRealtimeDatabase() = FirebaseDatabase
            .getInstance("https://tourist-app-2cea8-default-rtdb.europe-west1.firebasedatabase.app")

    @Provides
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()
}