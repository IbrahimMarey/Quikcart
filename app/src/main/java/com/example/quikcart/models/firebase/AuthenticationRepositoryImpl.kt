package com.example.quikcart.models.firebase

import com.example.quikcart.models.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject



class AuthRepositoryImp @Inject constructor() :AuthenticationRepository {

    private val auth: FirebaseAuth = Firebase.auth

    override suspend fun signUpWithEmailAndPassword(user: User): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(user.email, user.password).await()
            auth.currentUser?.sendEmailVerification()?.await()
            true
        } catch (e: Exception) {
            false
        }
    }
    override suspend fun signInWithEmailAndPassword(user: User): Boolean {
        return try {
            auth.signInWithEmailAndPassword(user.email, user.password).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}