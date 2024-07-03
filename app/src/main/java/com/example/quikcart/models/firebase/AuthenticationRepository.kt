package com.example.quikcart.models.firebase

import com.example.quikcart.models.entities.User

interface AuthenticationRepository {
    suspend fun signUpWithEmailAndPassword(user: User): Boolean
    suspend fun signInWithEmailAndPassword(user: User): Boolean // Add this line

}