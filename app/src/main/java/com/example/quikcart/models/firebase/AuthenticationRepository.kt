package com.example.quikcart.models.firebase

import com.example.quikcart.models.entities.User

interface AuthenticationRepository {
    suspend fun signUpWithEmailAndPassword(user: User):  Result<String>
    suspend fun signInWithEmailAndPassword(user: User): Result<String>
    fun logout()
    suspend fun resetPassword(email: String): Result<Unit>
}