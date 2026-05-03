package com.crystaldairyfarms.crystaldairyfarms.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crystaldairyfarms.crystaldairyfarms.data.CartItem
import com.crystaldairyfarms.crystaldairyfarms.data.room.CartDao
import com.crystaldairyfarms.crystaldairyfarms.data.room.toCartItem
import com.crystaldairyfarms.crystaldairyfarms.data.room.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartDao: CartDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            cartDao.getAllItems().collect { entities ->
                _uiState.update { it.copy(items = entities.map { e -> e.toCartItem() }) }
            }
        }
    }

    fun addItem(item: CartItem) {
        viewModelScope.launch {
            val existing = _uiState.value.items.find { it.id == item.id }
            val newQty = (existing?.quantity ?: 0) + 1
            cartDao.upsertItem(item.copy(quantity = newQty).toEntity())
        }
    }

    fun addItems(item: CartItem, count: Int) {
        if (count <= 0) return
        viewModelScope.launch {
            val existing = _uiState.value.items.find { it.id == item.id }
            val newQty = (existing?.quantity ?: 0) + count
            cartDao.upsertItem(item.copy(quantity = newQty).toEntity())
        }
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            val existing = _uiState.value.items.find { it.id == itemId } ?: return@launch
            if (existing.quantity > 1) {
                cartDao.upsertItem(existing.copy(quantity = existing.quantity - 1).toEntity())
            } else {
                cartDao.deleteItem(itemId)
            }
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch { cartDao.deleteItem(itemId) }
    }

    fun getQuantity(itemId: String): Int =
        _uiState.value.items.find { it.id == itemId }?.quantity ?: 0

    fun clearCart() {
        viewModelScope.launch { cartDao.clearCart() }
    }

    fun showCart() { _uiState.update { it.copy(isCartVisible = true) } }
    fun hideCart() { _uiState.update { it.copy(isCartVisible = false) } }
}