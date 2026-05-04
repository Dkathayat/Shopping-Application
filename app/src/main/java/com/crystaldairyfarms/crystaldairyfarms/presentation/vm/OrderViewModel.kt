package com.crystaldairyfarms.crystaldairyfarms.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crystaldairyfarms.crystaldairyfarms.data.Order
import com.crystaldairyfarms.crystaldairyfarms.data.OrderItem
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

private val _now get() = System.currentTimeMillis()
private val fallbackOrders = listOf(
    Order(
        orderId = "CRDA-00001",
        userName = "Deepak Singh Kathayat",
        userPhone = "+919999677471",
        items = listOf(
            OrderItem("1", "Fresh Milk",    "🥛", 2.49, 2),
            OrderItem("2", "Indian Paneer", "🧀", 4.19, 1),
            OrderItem("3", "Brown Bread",   "🍞", 1.99, 1),
            OrderItem("4", "Strawberries",  "🍓", 3.59, 1)
        ),
        total = 14.75,
        paymentMethod = "Cash on Delivery",
        status = "delivered",
        timestamp = _now - 86_400_000L,
        deliveryAddress = "Hno 123, Dwarka Mor, New Delhi - 110059"
    ),
    Order(
        orderId = "CRDA-00002",
        userName = "Deepak Singh Kathayat",
        userPhone = "+919999677471",
        items = listOf(
            OrderItem("5", "Orange Juice",  "🍊", 2.99, 2),
            OrderItem("6", "Greek Yoghurt", "🥛", 1.79, 2)
        ),
        total = 9.56,
        paymentMethod = "Google Pay",
        status = "confirmed",
        timestamp = _now - 3_600_000L,
        deliveryAddress = "Hno 123, Dwarka Mor, New Delhi - 110059"
    )
)

@HiltViewModel
class OrderViewModel @Inject constructor() : ViewModel() {

    private val ordersRef = FirebaseDatabase.getInstance().getReference("orders")

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val fetched = snapshot.children
                .mapNotNull { it.getValue(Order::class.java) }
                .sortedByDescending { it.timestamp }
            _orders.value = fetched.ifEmpty { fallbackOrders }
            _isLoading.value = false
        }

        override fun onCancelled(error: DatabaseError) {
            // Firebase failed — show dummy data instead of an empty screen
            if (_orders.value.isEmpty()) _orders.value = fallbackOrders
            _isLoading.value = false
        }
    }

    init {
        ordersRef.addValueEventListener(listener)
        // Safety net: if Firebase never responds within 5 seconds, show dummy data
        viewModelScope.launch {
            delay(5_000L)
            if (_isLoading.value) {
                if (_orders.value.isEmpty()) _orders.value = fallbackOrders
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ordersRef.removeEventListener(listener)
    }
}