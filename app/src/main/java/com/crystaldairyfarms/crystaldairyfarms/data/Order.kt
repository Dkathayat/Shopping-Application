package com.crystaldairyfarms.crystaldairyfarms.data

data class Order(
    val orderId: String = "",
    val userName: String = "",
    val userPhone: String = "",
    val items: List<OrderItem> = emptyList(),
    val total: Double = 0.0,
    val paymentMethod: String = "",
    val status: String = "pending",
    val timestamp: Long = 0L,
    val deliveryAddress: String = ""
)

data class OrderItem(
    val id: String = "",
    val name: String = "",
    val emoji: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1
)