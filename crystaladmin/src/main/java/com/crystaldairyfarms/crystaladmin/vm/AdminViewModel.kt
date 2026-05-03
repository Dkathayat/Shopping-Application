package com.crystaldairyfarms.crystaladmin.vm

import androidx.lifecycle.ViewModel
import com.crystaldairyfarms.crystaladmin.data.FirebaseProduct
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    object Success : UploadState()
    data class Error(val message: String) : UploadState()
}

@HiltViewModel
class AdminViewModel @Inject constructor() : ViewModel() {

    private val _products = MutableStateFlow<List<FirebaseProduct>>(emptyList())
    val products: StateFlow<List<FirebaseProduct>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    // IMPORTANT: Enable Firebase Realtime Database in the Firebase console and set rules:
    // { "rules": { ".read": true, ".write": true } } for development
    private val productsRef = FirebaseDatabase.getInstance().getReference("products")
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            _products.value = snapshot.children.mapNotNull { child ->
                child.getValue(FirebaseProduct::class.java)?.copy(id = child.key ?: "")
            }
            _isLoading.value = false
        }

        override fun onCancelled(error: DatabaseError) {
            _isLoading.value = false
            _uploadState.value = UploadState.Error(error.message)
        }
    }

    init {
        productsRef.addValueEventListener(listener)
    }

    fun addProduct(product: FirebaseProduct) {
        _uploadState.value = UploadState.Loading
        val ref = productsRef.push()
        val withId = product.copy(id = ref.key ?: "")
        ref.setValue(withId)
            .addOnSuccessListener { _uploadState.value = UploadState.Success }
            .addOnFailureListener { _uploadState.value = UploadState.Error(it.message ?: "Upload failed") }
    }

    fun deleteProduct(productId: String) {
        productsRef.child(productId).removeValue()
    }

    fun resetUploadState() {
        _uploadState.value = UploadState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        productsRef.removeEventListener(listener)
    }
}