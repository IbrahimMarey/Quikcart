package com.example.quikcart.models.firebase

interface AuthenticationRepository {
    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<Unit>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Unit>
}