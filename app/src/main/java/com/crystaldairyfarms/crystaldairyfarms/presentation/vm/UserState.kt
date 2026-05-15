package com.crystaldairyfarms.crystaldairyfarms.presentation.vm

import com.google.firebase.auth.FirebaseUser

sealed class UserState {
    object Idle : UserState()
    object Loading : UserState()
    object Guest : UserState()
    data class SignedIn(val user: FirebaseUser) : UserState()
    data class Error(val message: String) : UserState()
}