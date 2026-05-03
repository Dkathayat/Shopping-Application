package com.crystaldairyfarms.crystaldairyfarms.presentation.vm

import androidx.lifecycle.ViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.FirebaseProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectedProductViewModel : ViewModel() {
    private val _product = MutableStateFlow<FirebaseProduct?>(null)
    val product: StateFlow<FirebaseProduct?> = _product.asStateFlow()

    fun select(product: FirebaseProduct) {
        _product.value = product
    }
}