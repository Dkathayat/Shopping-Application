package com.crystaldairyfarms.crystaladmin.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crystaldairyfarms.crystaladmin.data.FirebaseProduct
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val dummyProducts = listOf(
    FirebaseProduct("d1", "Fresh Cow Milk",    "Crystal Dairy",  "1 Litre",  2.49, "🥛", "Dairy"),
    FirebaseProduct("d2", "Indian Paneer",     "Crystal Dairy",  "200 gm",   4.19, "🧀", "Dairy"),
    FirebaseProduct("d3", "Brown Bread",       "Daily Bakes",    "400 gm",   1.99, "🍞", "Breads"),
    FirebaseProduct("d4", "Fresh Strawberries","Farm Fresh",     "250 gm",   3.59, "🍓", "Fruits"),
    FirebaseProduct("d5", "Orange Juice",      "Crystal Dairy",  "500 ml",   2.99, "🍊", "Beverages"),
    FirebaseProduct("d6", "Greek Yoghurt",     "Crystal Dairy",  "200 gm",   1.79, "🥛", "Dairy"),
    FirebaseProduct("d7", "Sourdough Bread",   "Daily Bakes",    "500 gm",   3.49, "🥖", "Breads"),
    FirebaseProduct("d8", "Alphonso Mango",    "Farm Fresh",     "1 kg",     5.99, "🥭", "Fruits"),
)

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
            val fetched = snapshot.children.mapNotNull { child ->
                child.getValue(FirebaseProduct::class.java)?.copy(id = child.key ?: "")
            }
            _products.value = fetched.ifEmpty { dummyProducts }
            _isLoading.value = false
        }

        override fun onCancelled(error: DatabaseError) {
            if (_products.value.isEmpty()) _products.value = dummyProducts
            _isLoading.value = false
            _uploadState.value = UploadState.Error(error.message)
        }
    }

    init {
        productsRef.addValueEventListener(listener)
        viewModelScope.launch {
            delay(5_000L)
            if (_isLoading.value) {
                if (_products.value.isEmpty()) _products.value = dummyProducts
                _isLoading.value = false
            }
        }
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