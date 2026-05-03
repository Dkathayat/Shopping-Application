package com.crystaldairyfarms.crystaldairyfarms.presentation.vm

import com.crystaldairyfarms.crystaldairyfarms.data.CartItem

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val isCartVisible: Boolean = false
) {
    // Derived properties — always in sync, no manual tracking needed
    val totalItems: Int get() = items.sumOf { it.quantity }
    val totalPrice: Double get() = items.sumOf { it.price * it.quantity }
    val isEmpty: Boolean get() = items.isEmpty()
}