package com.crystaldairyfarms.crystaladmin.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crystaldairyfarms.crystaladmin.data.Order
import com.crystaldairyfarms.crystaladmin.data.OrderItem
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

private val now = System.currentTimeMillis()
private val dummyOrders = listOf(
    Order(
        orderId = "CRDA-10021",
        userName = "Priya Sharma",
        userPhone = "+919812345678",
        items = listOf(
            OrderItem("1", "Fresh Cow Milk",  "🥛", 2.49, 2),
            OrderItem("2", "Indian Paneer",   "🧀", 4.19, 1),
            OrderItem("3", "Brown Bread",     "🍞", 1.99, 1)
        ),
        total = 11.16,
        paymentMethod = "Google Pay",
        status = "delivered",
        timestamp = now - 2 * 86_400_000L,
        deliveryAddress = "B-42, Saket, New Delhi - 110017"
    ),
    Order(
        orderId = "CRDA-10034",
        userName = "Rahul Verma",
        userPhone = "+917654321098",
        items = listOf(
            OrderItem("4", "Strawberries",    "🍓", 3.59, 2),
            OrderItem("5", "Orange Juice",    "🍊", 2.99, 1),
            OrderItem("6", "Greek Yoghurt",   "🥛", 1.79, 3)
        ),
        total = 15.53,
        paymentMethod = "PhonePe",
        status = "out for delivery",
        timestamp = now - 3_600_000L,
        deliveryAddress = "C-11, Vasant Kunj, New Delhi - 110070"
    ),
    Order(
        orderId = "CRDA-10047",
        userName = "Anita Nair",
        userPhone = "+918888777766",
        items = listOf(
            OrderItem("7", "Sourdough Bread", "🥖", 3.49, 1),
            OrderItem("8", "Alphonso Mango",  "🥭", 5.99, 1)
        ),
        total = 9.48,
        paymentMethod = "Cash on Delivery",
        status = "pending",
        timestamp = now - 900_000L,
        deliveryAddress = "F-5, Dwarka Sector 6, New Delhi - 110075"
    ),
    Order(
        orderId = "CRDA-10058",
        userName = "Deepak Singh Kathayat",
        userPhone = "+919999677471",
        items = listOf(
            OrderItem("1", "Fresh Cow Milk",  "🥛", 2.49, 3),
            OrderItem("2", "Indian Paneer",   "🧀", 4.19, 2),
            OrderItem("3", "Brown Bread",     "🍞", 1.99, 2),
            OrderItem("5", "Orange Juice",    "🍊", 2.99, 2)
        ),
        total = 24.31,
        paymentMethod = "Paytm",
        status = "confirmed",
        timestamp = now - 1_800_000L,
        deliveryAddress = "Hno 123, Dwarka Mor, New Delhi - 110059"
    )
)

@HiltViewModel
class AdminOrderViewModel @Inject constructor() : ViewModel() {

    private val ordersRef = FirebaseDatabase.getInstance().getReference("orders")

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Tracks new orders that arrived while admin is on screen - for in-app notification
    private val _newOrderCount = MutableStateFlow(0)
    val newOrderCount: StateFlow<Int> = _newOrderCount.asStateFlow()

    private var initialLoadDone = false
    private var lastKnownCount = 0

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val fetched = snapshot.children
                .mapNotNull { it.getValue(Order::class.java) }
                .sortedByDescending { it.timestamp }
            if (initialLoadDone && fetched.size > lastKnownCount) {
                _newOrderCount.value = fetched.size - lastKnownCount
            }
            lastKnownCount = fetched.size
            initialLoadDone = true
            _orders.value = fetched.ifEmpty { dummyOrders }
            _isLoading.value = false
        }

        override fun onCancelled(error: DatabaseError) {
            if (_orders.value.isEmpty()) _orders.value = dummyOrders
            _isLoading.value = false
        }
    }

    init {
        ordersRef.addValueEventListener(listener)
        viewModelScope.launch {
            delay(5_000L)
            if (_isLoading.value) {
                if (_orders.value.isEmpty()) _orders.value = dummyOrders
                _isLoading.value = false
            }
        }
    }

    fun clearNewOrderNotification() {
        _newOrderCount.value = 0
    }

    fun updateOrderStatus(orderId: String, status: String) {
        ordersRef.child(orderId).child("status").setValue(status)
    }

    override fun onCleared() {
        super.onCleared()
        ordersRef.removeEventListener(listener)
    }
}