package com.crystaldairyfarms.crystaldairyfarms.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crystaldairyfarms.crystaldairyfarms.data.CartItem
import com.crystaldairyfarms.crystaldairyfarms.data.Order
import com.crystaldairyfarms.crystaldairyfarms.data.OrderItem
import com.crystaldairyfarms.crystaldairyfarms.data.room.CartDao
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class OrderState {
    object Idle : OrderState()
    object Loading : OrderState()
    data class Success(val orderId: String) : OrderState()
    data class Error(val message: String) : OrderState()
}

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartDao: CartDao
) : ViewModel() {

    private val ordersRef = FirebaseDatabase.getInstance().getReference("orders")

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState.asStateFlow()

    fun placeOrder(items: List<CartItem>, paymentMethod: String, deliveryAddress: String) {
        if (items.isEmpty()) return
        _orderState.value = OrderState.Loading

        val orderId = "CRDA-${(10000..99999).random()}"
        val order = Order(
            orderId = orderId,
            userName = "Deepak Singh Kathayat",
            userPhone = "+919999677471",
            items = items.map { OrderItem(it.id, it.name, it.emoji, it.price, it.quantity) },
            total = items.sumOf { it.price * it.quantity },
            paymentMethod = paymentMethod,
            status = "pending",
            timestamp = System.currentTimeMillis(),
            deliveryAddress = deliveryAddress
        )

        // Fire-and-forget — save to Firebase in background, don't block the user
        ordersRef.child(orderId).setValue(order)

        // Clear cart and show success immediately without waiting for Firebase
        viewModelScope.launch {
            cartDao.clearCart()
            _orderState.value = OrderState.Success(orderId)
        }
    }

    fun reset() { _orderState.value = OrderState.Idle }
}