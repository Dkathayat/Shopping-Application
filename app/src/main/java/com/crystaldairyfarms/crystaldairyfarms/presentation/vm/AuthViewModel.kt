package com.crystaldairyfarms.crystaldairyfarms.presentation.vm

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    companion object {
        const val WEB_CLIENT_ID =
            "775528031998-1rbn0cvjcn9hunmbub5ol9shlk6196sv.apps.googleusercontent.com"
    }

    private val _userState = MutableStateFlow<UserState>(UserState.Idle)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    init {
        // Restore previously signed-in Google session
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _userState.value = UserState.SignedIn(currentUser)
        }
    }

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val credentialManager = CredentialManager.create(context)
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(WEB_CLIENT_ID)
                    .setAutoSelectEnabled(false)
                    .build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                val result = credentialManager.getCredential(context = context, request = request)
                handleCredential(result)
            } catch (e: GetCredentialException) {
                _userState.value = UserState.Error("Sign-in cancelled. Please try again.")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Something went wrong. Please try again.")
            }
        }
    }

    private suspend fun handleCredential(result: GetCredentialResponse) {
        val credential = result.credential
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                val authResult = suspendCoroutine { cont ->
                    auth.signInWithCredential(firebaseCredential)
                        .addOnSuccessListener { cont.resume(it) }
                        .addOnFailureListener { cont.resumeWithException(it) }
                }
                val user = authResult.user
                if (user != null) {
                    _userState.value = UserState.SignedIn(user)
                } else {
                    _userState.value = UserState.Error("Sign-in failed. Please try again.")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error(
                    "Authentication failed: ${e.localizedMessage ?: "Please try again."}"
                )
            }
        } else {
            _userState.value = UserState.Error("Unsupported credential type.")
        }
    }

    fun continueAsGuest() {
        _userState.value = UserState.Guest
    }

    fun clearError() {
        if (_userState.value is UserState.Error) {
            _userState.value = UserState.Idle
        }
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            auth.signOut()
            runCatching {
                CredentialManager.create(context)
                    .clearCredentialState(ClearCredentialStateRequest())
            }
            _userState.value = UserState.Idle
        }
    }
}