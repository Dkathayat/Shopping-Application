package com.crystaldairyfarms.crystaldairyfarms.presentation.vm

import androidx.lifecycle.ViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun addItem(item: CartItem) {
        _uiState.update { state ->
            val existing = state.items.find { it.id == item.id }
            if (existing != null) {
                state.copy(
                    items = state.items.map {
                        if (it.id == item.id) it.copy(quantity = it.quantity + 1) else it
                    }
                )
            } else {
                state.copy(items = state.items + item.copy(quantity = 1))
            }
        }
    }

    // ── Remove one quantity ───────────────────────────────────────────────────
    // If quantity reaches 0 → remove item from list entirely
    fun removeItem(itemId: String) {
        _uiState.update { state ->
            val existing = state.items.find { it.id == itemId }
            if (existing != null && existing.quantity > 1) {
                state.copy(
                    items = state.items.map {
                        if (it.id == itemId) it.copy(quantity = it.quantity - 1) else it
                    }
                )
            } else {
                state.copy(items = state.items.filter { it.id != itemId })
            }
        }
    }

    // ── Delete item completely regardless of quantity ──────────────────────────
    fun deleteItem(itemId: String) {
        _uiState.update { state ->
            state.copy(items = state.items.filter { it.id != itemId })
        }
    }

    // ── Get quantity of a specific item (for showing +/- on product cards) ────
    fun getQuantity(itemId: String): Int {
        return _uiState.value.items.find { it.id == itemId }?.quantity ?: 0
    }

    // ── Clear entire cart ─────────────────────────────────────────────────────
    fun clearCart() {
        _uiState.update { it.copy(items = emptyList()) }
    }

    // ── Toggle cart sheet visibility ──────────────────────────────────────────
    fun showCart() {
        _uiState.update { it.copy(isCartVisible = true) }
    }

    fun hideCart() {
        _uiState.update { it.copy(isCartVisible = false) }
    }
}